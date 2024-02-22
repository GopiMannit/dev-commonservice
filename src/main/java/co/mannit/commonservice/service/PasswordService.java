package co.mannit.commonservice.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import co.mannit.commonservice.ServiceCommonException;
import co.mannit.commonservice.common.MongokeyvaluePair;
import co.mannit.commonservice.common.ValueExtracterFromJSON;
import co.mannit.commonservice.common.util.DateUtil;
import co.mannit.commonservice.common.util.ValidationUtil;
import co.mannit.commonservice.crypto.AESSymmetricEncryption;
import co.mannit.commonservice.dao.UserDao;
import co.mannit.commonservice.pojo.User;
import co.mannit.commonservice.validator.PasswordValidator;

@Service
public class PasswordService {

	private static final Logger logger = LogManager.getLogger(LoginService.class);
	
	@Autowired
	private ValueExtracterFromJSON valueExtracterFromJSON;
	
	@Autowired
	private UserDao logisignupassDao;
	
	@Autowired
	private AESSymmetricEncryption aesSymmetricEncryption;
	
	@Autowired
	private PasswordValidator passwordValidator;
	
	public String forgetPassword(String loginDetails) throws Exception {
		return null;
	}
	
	public String resetpwd(String loginDetails) throws Exception {
		logger.debug("<login> loginDetails:{} method:{}",loginDetails);
		
		String msg = "";
		String userDetails = null;
		
		String userName = valueExtracterFromJSON.getValue(loginDetails, "username", String.class);
		String mobileNumber = String.valueOf(valueExtracterFromJSON.getValue(loginDetails, "mobileno", Long.class));
		String password = String.valueOf(valueExtracterFromJSON.getValue(loginDetails, "password", String.class));
		String newPassword = String.valueOf(valueExtracterFromJSON.getValue(loginDetails, "new_password", String.class));
		
		if(!passwordValidator.isValid(newPassword)) {throw new ServiceCommonException("105");}
		
		List<MongokeyvaluePair<? extends Object>> lstPairs = new ArrayList<>();
		lstPairs.add(new MongokeyvaluePair<String>("password", aesSymmetricEncryption.encryptAsString(password, aesSymmetricEncryption.getSecretKey())));
		
		if(StringUtils.hasText(userName)) {
			lstPairs.add(new MongokeyvaluePair<String>("username", userName));
		}else if(StringUtils.hasText(mobileNumber)) {
			lstPairs.add(new MongokeyvaluePair<Long>("mobileno", Long.valueOf(mobileNumber)));
		}else {
			throw new ServiceCommonException("103", new String[] {"login crendential"});
		}
		
		userDetails = logisignupassDao.findDocAsString(lstPairs);
		
		if (userDetails == null) {throw new ServiceCommonException("103", new String[] {"login crendential"});};
		
		
		String loggedinTime = valueExtracterFromJSON.getValue(loginDetails, "loggedintime", String.class);
		
		List<MongokeyvaluePair<? extends Object>> pairs = new ArrayList<>();
		pairs.add(new MongokeyvaluePair<String>("loggedintime", DateUtil.getCurrentDateTime()));
		pairs.add(new MongokeyvaluePair<String>("lastloggedintime", loggedinTime));
		pairs.add(new MongokeyvaluePair<String>("password", aesSymmetricEncryption.encryptAsString(newPassword, aesSymmetricEncryption.getSecretKey())));
		logisignupassDao.saveDocument(userDetails, pairs);
		msg = "Password successfully created";
//		lstPairs.add(new MongokeyvaluePair<Integer>("otp", otp));
//		lstPairs.add(new MongokeyvaluePair<Long>("otptime", System.currentTimeMillis()));
//		lstPairs.add(new MongokeyvaluePair<String>("otpchannel", method.toString()));
//		lstPairs.add(new MongokeyvaluePair<String>("otpstatus", "Success"));
		
		logger.debug("</login>");
		return msg;
	}
	
	public String forgetPassword(User user) throws Exception {
		
		if(!ValidationUtil.validateDomainAndSubDomain(user.getDomain(), user.getSubdomain())) {
			throw new ServiceCommonException("100");
		}
		
		if(ValidationUtil.isNotValidMobileNo(user.getMobileno())) throw new ServiceCommonException("103", new String[]{"mobile number"});
		
		List<MongokeyvaluePair<? extends Object>> keyValuePairs = new ArrayList<>();

		keyValuePairs.add(new MongokeyvaluePair<String>("domain", user.getDomain()));
		keyValuePairs.add(new MongokeyvaluePair<String>("subdomain", user.getSubdomain()));
		keyValuePairs.add(new MongokeyvaluePair<Long>("mobileno", Long.parseLong(user.getMobileno())));
		
		Document doc = logisignupassDao.findDoc(keyValuePairs);
		
		if(doc == null )throw new ServiceCommonException("103", new String[]{"user details"});
		
		if(passwordValidator.isNotValid(user.getPassword())) {throw new ServiceCommonException("105");}
		
		doc.put("password", aesSymmetricEncryption.encryptAsString(user.getPassword(), aesSymmetricEncryption.getSecretKey()));
		
		logisignupassDao.saveDocument(doc);
		
		doc.put("password", "");
		
		return doc.toJson();
	}
}
