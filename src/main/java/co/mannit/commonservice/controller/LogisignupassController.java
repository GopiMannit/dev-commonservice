package co.mannit.commonservice.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import co.mannit.commonservice.common.LoginMethod;
import co.mannit.commonservice.common.Response;
import co.mannit.commonservice.service.LogisignupassService;



//@RestController
public class LogisignupassController{

	private static final Logger logger = LogManager.getLogger(LogisignupassController.class);
	
	@Autowired
	private LogisignupassService logisignupassService;
	
	@GetMapping("signup")
	public Response<?> signup(@RequestBody String userDetails) throws Exception {
		logger.debug("<register> {} ",userDetails);
		logisignupassService.registerUser(userDetails);
		logger.debug("</register>");
		return Response.buildSuccessMsg(200, "User Registered Successfully", null);
	}
	
	@GetMapping("login/{method}")
	public Response<?> login(@RequestBody String loginDetails, @PathVariable String method) throws Exception {
		logger.debug("<register> {} ",method);
		logger.debug("<register> {} ",loginDetails);
		String msg = logisignupassService.login(loginDetails, LoginMethod.value(method));
		logger.debug("</register>");
		
		return Response.buildSuccessMsg(200, msg, null);
	}
	
	@GetMapping("verifyotp/{method}")
	public Response<?> verifyotp(@RequestBody String loginDetails, @PathVariable String method) throws Exception {
		logger.debug("<verifyotp> {} ",loginDetails);
		String msg = logisignupassService.verifyOTP(loginDetails, LoginMethod.value(method));
		logger.debug("</verifyotp>");
		return Response.buildSuccessMsg(200, msg, null);
	}
	
	@GetMapping("createnewpwd/{method}")
	public Response<?> createnewunpw(@RequestBody String loginDetails, @PathVariable String method) throws Exception {
		logger.debug("<createnewpwd> {} ",loginDetails);
		String msg = logisignupassService.createnewpwd(loginDetails, LoginMethod.value(method));
		logger.debug("</createnewpwd>");
		return Response.buildSuccessMsg(200, msg, null);
	}
	
	
	
	/*@GetMapping("forgetpassword/{method}")
	public void forgetPassword(@RequestBody String userDetails) throws ServiceCommonException {
		logger.debug("<register> {} ",userDetails);
		logisignupassService.registerUser(userDetails);
		logger.debug("</register>");
	}*/
	
}
