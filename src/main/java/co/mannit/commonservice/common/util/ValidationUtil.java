package co.mannit.commonservice.common.util;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import co.mannit.commonservice.ServiceCommonException;
import co.mannit.commonservice.pojo.BaseReqParam;

public class ValidationUtil {

	static Pattern ptrn = Pattern.compile("^\\d{10}$");
	
	static public boolean validateDomainAndId(String domain, String subDomain, String id) {
		if(!StringUtils.hasLength(domain) || !StringUtils.hasLength(subDomain) || !StringUtils.hasLength(id)) {
			return false;
		}
		
		return true;
	}
	
	static public boolean validateDomainAndSubDomain(String domain, String subDomain) {
		if(!StringUtils.hasLength(domain) || !StringUtils.hasLength(subDomain)) {
			return false;
		}
		
		return true;
	}
	
	static public boolean validateDomainAndId(BaseReqParam baseReqParam) {
		return validateDomainAndId(baseReqParam.getDomain(), baseReqParam.getSubdomain(), baseReqParam.getUserId());
	}
	
	static public boolean validateDomainAndSubDomain(BaseReqParam baseReqParam) {
		return validateDomainAndSubDomain(baseReqParam.getDomain(), baseReqParam.getSubdomain());
	}
	
	static public boolean isValidMobileNo(String mobileno) {
		boolean isValidMobileno = true;
		if(mobileno == null || !ptrn.matcher(String.valueOf(mobileno)).matches()){
			isValidMobileno = false;
		}
		return isValidMobileno;
	}
	
	static public boolean isNotValidMobileNo(String mobileno) {
		return !isValidMobileNo(mobileno);
	}
}
