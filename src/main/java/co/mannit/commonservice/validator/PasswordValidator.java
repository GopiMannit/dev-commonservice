package co.mannit.commonservice.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class PasswordValidator {

	private String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$";
	private Pattern ptrn = Pattern.compile(regex);
	
	/**It contains at least 8 characters and at most 20 characters.
	It contains at least one digit.
	It contains at least one upper case alphabet.
	It contains at least one lower case alphabet.
	It contains at least one special character which includes !@#$%&*()-+=^.
	It doesn’t contain any white space.**/
	public boolean isValid(String password) {
		boolean isValid =  ptrn.matcher(password).matches();
		return isValid;
	}
	
	public boolean isNotValid(String password) {
		return !isValid(password);
	}
	
	
	public static void main(String[] args) {
		PasswordValidator pwdValidator = new PasswordValidator();
		System.out.println("less than eight :"+pwdValidator.isValid("1ddsD@"));
		System.out.println("more than eight :"+pwdValidator.isValid("1ddsD@weqweqw2132sdasdq221"));
		System.out.println("didgit missig :"+pwdValidator.isValid("Abcdefghi@"));
		System.out.println("uppercase missing :"+pwdValidator.isValid("abcdefghi1@"));
		System.out.println("lower case missing :"+pwdValidator.isValid("ABCDEFGHI1@"));
		System.out.println("special char missing :"+pwdValidator.isValid("Abcdefghi1"));
		System.out.println("WhiteSpace :"+pwdValidator.isValid("Abcde fghi1@"));
		System.out.println("correct1 :"+pwdValidator.isValid("Abcdefghi1@"));
		System.out.println("correct2 :"+pwdValidator.isValid("Bnmdhjtep2$"));
		
	}
}
