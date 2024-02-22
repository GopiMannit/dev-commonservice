package co.mannit.commonservice.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import co.mannit.commonservice.ServiceCommonException;
import co.mannit.commonservice.common.LoginMethod;
import co.mannit.commonservice.common.MongokeyvaluePair;
import co.mannit.commonservice.common.ValueExtracterFromJSON;
import co.mannit.commonservice.crypto.AESSymmetricEncryption;
import co.mannit.commonservice.dao.UserDao;
import co.mannit.commonservice.external.MsgSender;
import co.mannit.commonservice.po.MsgDetails;



//@Service
public class LogisignupassService {

	private static final Logger logger = LogManager.getLogger(LogisignupassService.class);
			
	@Autowired
	private ValueExtracterFromJSON valueExtracterFromJSON;
	
	@Autowired
	private UserDao logisignupassDao;
	
	@Autowired
	AESSymmetricEncryption aesSymmetricEncryption;
	
	public String registerUser(String registrationDetails) throws Exception{
		logger.debug("<registerUser> registrationDetails:{}",registrationDetails);
		
		String userName = valueExtracterFromJSON.getValue(registrationDetails, "username", String.class);
		String email = valueExtracterFromJSON.getValue(registrationDetails, "email", String.class);
		String mobileno = String.valueOf(valueExtracterFromJSON.getValue(registrationDetails, "mobileno", Long.class));
		String password = String.valueOf(valueExtracterFromJSON.getValue(registrationDetails, "password", String.class));
		
		logisignupassDao.printAllDoc();
		if(!StringUtils.hasText(userName) || !StringUtils.hasText(email) || !StringUtils.hasText(password)) {
			throw new ServiceCommonException("100");
		}
		if(!StringUtils.hasText(mobileno) || mobileno.length() != 10) {
			throw new ServiceCommonException("100");
		}
		
		boolean isUserNameExist = logisignupassDao.isUserAlreadyExist("username", userName);
		boolean isEmailExist = logisignupassDao.isUserAlreadyExist("email", email);
		boolean isMobilenoExist = logisignupassDao.isUserAlreadyExist("mobileno", mobileno);
		
		if(isUserNameExist || isEmailExist || isMobilenoExist) {
			throw new ServiceCommonException("101");
		}
		
		List<MongokeyvaluePair<? extends Object>> keyValuePairs = new ArrayList<>();
		keyValuePairs.add(new MongokeyvaluePair<String>("password", aesSymmetricEncryption.encryptAsString(password, aesSymmetricEncryption.getSecretKey()) ));
		
		logisignupassDao.insertDocument(registrationDetails, keyValuePairs);
		
		logger.debug("</registerUser>");
		return "User Successfully Registered";
	}
	
	public String login(String loginDetails, LoginMethod method) throws Exception {
		logger.debug("<login> loginDetails:{} method:{}",loginDetails,method);
		
		String msg = "";
		String userDetails = null;
		
		if(LoginMethod.UNPW == method) {
			String userName = valueExtracterFromJSON.getValue(loginDetails, "username", String.class);
			String password = String.valueOf(valueExtracterFromJSON.getValue(loginDetails, "password", String.class));
			
			List<MongokeyvaluePair<? extends Object>> lstPairs = new ArrayList<>();
			lstPairs.add(new MongokeyvaluePair<String>("username", userName));
			lstPairs.add(new MongokeyvaluePair<String>("password", aesSymmetricEncryption.encryptAsString(password, aesSymmetricEncryption.getSecretKey())));
			
			userDetails = logisignupassDao.findDocAsString(lstPairs);
			if (userDetails == null) {throw new ServiceCommonException("Invalid login crendential");};
			msg = "Loggedin Sucessfully";
		}else{
			
			int otp = getRandomNumber();
			MsgDetails msgDetails = null;
			MsgSender msgSender = MsgSender.createMsgSender(method);
			
			if(LoginMethod.MOBILENO == method) { 
				long mobileno = valueExtracterFromJSON.getValue(loginDetails, "mobileno", Long.class);
				
				userDetails = logisignupassDao.findDocAsString(new MongokeyvaluePair<Long>("mobileno", mobileno));
				if (userDetails == null) {throw new ServiceCommonException("Entered Mobile number not registered");}
				
				String otpmsg = "Use the following OTP to login app";
				msgDetails = MsgDetails.builder().toAddress(String.valueOf(mobileno)).fromAddress("").msg(otpmsg).build();
				msgSender.sendMessage(msgDetails);
				
				msg = "OTP sent to your mobile number";
			}else if(LoginMethod.EMAIL == method) {
				String email = valueExtracterFromJSON.getValue(loginDetails, "email", String.class);
				
				userDetails = logisignupassDao.findDocAsString(new MongokeyvaluePair<String>("email", email));
				if (userDetails == null) {throw new ServiceCommonException("Entered email not registered");}
				
				String otpmsg = "Use the following OTP to login app";
				msgDetails = MsgDetails.builder().toAddress(email).fromAddress("").msg(otpmsg).build();
				msgSender.sendMessage(msgDetails);
				
				msg = "OTP sent to your email";
			}
			
			
			List<MongokeyvaluePair<? extends Object>> lstPairs = new ArrayList<>();
			lstPairs.add(new MongokeyvaluePair<Integer>("otp", otp));
			lstPairs.add(new MongokeyvaluePair<Long>("otptime", System.currentTimeMillis()));
			lstPairs.add(new MongokeyvaluePair<String>("otpchannel", method.toString()));
			lstPairs.add(new MongokeyvaluePair<String>("otpstatus", "Success"));
			logisignupassDao.saveDocument(userDetails, lstPairs);
		}

		
		logger.debug("</login>");
		return msg;
	}
	
	public String verifyOTP(String loginDetails, LoginMethod method) throws Exception {
		logger.debug("</verifyOTP> loginDetails:{} method:{}",loginDetails,method);
		String userDetails = null;
		
		
		if(LoginMethod.MOBILENO == method) {
			long mobileno = valueExtracterFromJSON.getValue(loginDetails, "mobileno", Long.class);
			userDetails = logisignupassDao.findDocAsString(new MongokeyvaluePair<Long>("mobileno", mobileno));
			
			if (userDetails == null) {throw new ServiceCommonException("Entered Mobile number not registered");}
			
		}else if(LoginMethod.EMAIL == method) {
			String email = valueExtracterFromJSON.getValue(loginDetails, "email", String.class);
			userDetails = logisignupassDao.findDocAsString(new MongokeyvaluePair<String>("email", email));
			
			if (userDetails == null) {throw new ServiceCommonException("Entered email id not registered");}
		}
		
		int reqOTP = valueExtracterFromJSON.getValue(loginDetails, "otp", Integer.class);
		int savOTP = valueExtracterFromJSON.getValue(userDetails, "otp", Integer.class);
		
		logger.debug("reqOTP:{}",reqOTP);
		logger.debug("savOTP:{}",savOTP);
		if(reqOTP == savOTP) {
			throw new ServiceCommonException("102");
		}
		
		logger.debug("</verifyOTP>");
		return "OTP verified successfully";
	}
	
	public String createnewpwd(String userLoginDetails, LoginMethod method) throws Exception {
		logger.debug("</createnewpwd> loginDetails:{} method:{}",userLoginDetails,method);
		
		String userDetails = null;
		String password = valueExtracterFromJSON.getValue(userLoginDetails, "password", String.class);
		
		if(!StringUtils.hasText(password)) {
			throw new ServiceCommonException("100");
		}
		
		if(LoginMethod.MOBILENO == method) {
			long mobileno = valueExtracterFromJSON.getValue(userLoginDetails, "mobileno", Long.class);
			userDetails = logisignupassDao.findDocAsString(new MongokeyvaluePair<Long>("mobileno", mobileno));
			
			if (userDetails == null) {throw new ServiceCommonException("Entered Mobile number not registered");}
			
		}else if(LoginMethod.EMAIL == method) {
			String email = valueExtracterFromJSON.getValue(userLoginDetails, "email", String.class);
			userDetails = logisignupassDao.findDocAsString(new MongokeyvaluePair<String>("email", email));
			
			if (userDetails == null) {throw new ServiceCommonException("Entered email id not registered");}
		}
		
		List<MongokeyvaluePair<? extends Object>> lstPairs = new ArrayList<>();
		lstPairs.add(new MongokeyvaluePair<String>("password", aesSymmetricEncryption.encryptAsString(password, aesSymmetricEncryption.getSecretKey())));
		logisignupassDao.saveDocument(userDetails, lstPairs);
		
		logger.debug("</createnewpwd>");
		return "Password successfully created";
	}
	
	
	private int getRandomNumber() {
		int min = 100000;
		int max = 999999;
		int randomNumber = (int) (Math.random() * (max - min + 1)) + min;
		return randomNumber;
	}
}
