package co.mannit.commonservice.common;

public enum LoginMethod {

	UNPW,EMAIL,MOBILENO;
	
	public static LoginMethod value(String method) {
		switch(method) {
		case "uspw":
			return LoginMethod.UNPW;
		case "email":
			return LoginMethod.EMAIL;
		case "mobileno":
			return LoginMethod.MOBILENO;
		default:
			return null;
		}
	}
}
