package co.mannit.commonservice.pojo;



public class BaseReqParam {

	private String domain;
	private String subdomain;
	private String userId;
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getSubdomain() {
		return subdomain;
	}
	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	@Override
	public String toString() {
		return "BaseReqParam [domain=" + domain + ", subdomain=" + subdomain + ", userId=" + userId + "]";
	}
	
	
}
