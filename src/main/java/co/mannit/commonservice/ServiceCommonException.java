package co.mannit.commonservice;

public class ServiceCommonException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	private String[] params;
	
	public ServiceCommonException() {
	}
	
	public ServiceCommonException(String errorCode) {
		this(errorCode, null);
	}
	
	public ServiceCommonException(String errorCode, String[] params) {
		this.errorCode = errorCode;
		this.params = params;
	}
	
	/*public ServiceCommonException(String msg) {
		super(msg);
	}*/
	
	public String getMessage() {
		return super.getMessage();
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}
	
}
