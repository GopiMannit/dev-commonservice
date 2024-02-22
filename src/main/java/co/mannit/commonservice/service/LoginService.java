package co.mannit.commonservice.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import co.mannit.commonservice.ServiceCommonException;
import co.mannit.commonservice.common.MongokeyvaluePair;
import co.mannit.commonservice.common.ValueExtracterFromJSON;
import co.mannit.commonservice.common.util.DateUtil;
import co.mannit.commonservice.crypto.AESSymmetricEncryption;
import co.mannit.commonservice.dao.UserDao;

@Service
public class LoginService {

	private static final Logger logger = LogManager.getLogger(LoginService.class);
	
	@Autowired
	private ValueExtracterFromJSON valueExtracterFromJSON;
	
	@Autowired
	private UserDao logisignupassDao;
	
	@Autowired
	private AESSymmetricEncryption aesSymmetricEncryption;
			
	public String login(String loginDetails) throws Exception {
		logger.debug("<login> loginDetails:{} method:{}",loginDetails);
		
		String userDetails = null;
		
		String userName = valueExtracterFromJSON.getValue(loginDetails, "username", String.class);
		String mobileNumber = String.valueOf(valueExtracterFromJSON.getValue(loginDetails, "mobileno", Long.class));
		String password = String.valueOf(valueExtracterFromJSON.getValue(loginDetails, "password", String.class));
		
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
		logisignupassDao.saveDocument(userDetails, lstPairs);
		
//		lstPairs.add(new MongokeyvaluePair<Integer>("otp", otp));
//		lstPairs.add(new MongokeyvaluePair<Long>("otptime", System.currentTimeMillis()));
//		lstPairs.add(new MongokeyvaluePair<String>("otpchannel", method.toString()));
//		lstPairs.add(new MongokeyvaluePair<String>("otpstatus", "Success"));
		
		logger.debug("</login>");
		return userDetails;
	}
}
