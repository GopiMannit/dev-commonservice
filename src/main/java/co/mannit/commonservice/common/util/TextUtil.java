package co.mannit.commonservice.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TextUtil {

	public static boolean isEmpty(String value) {
		
		if(value == null || "".equals(value.trim()) || "null".equals(value.trim())) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}
	
	
	public static String getStackTrace(Throwable t) {
		String value = "";
		if(t != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			value = sw.toString();
		}
		
		return value;
	}
	
	public static String removeNull(String value) {
		if(value == null || "null".equals(value.trim())) {
			return "";
		}
		
		return value;
	}
	
	public static String constructValueForInClause(String[] values) {
		if(values == null || values.length == 0) return "";
		return Arrays.stream(values).filter(str -> str != null && str.length() >0).map(str -> "'"+str+"'").collect(Collectors.joining(","));
	}
	
	public static void main(String[] args) {
		String[] value = {};
		System.out.println("Empty:"+constructValueForInClause(value));
		value = null;
		System.out.println("Null:"+constructValueForInClause(value));
		value = new String[]{"Apple"};
		System.out.println("One:"+constructValueForInClause(value));
		value = new String[]{""};
		System.out.println("One Empty:"+constructValueForInClause(value));
		value = new String[]{"Appple",null,"Orange","","JackFruit", "Mango"};
		System.out.println("All:"+constructValueForInClause(value));
	}
	
	public static String formatApplicationNumber(long value) {
		String pattern = "0000000";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		return decimalFormat.format(value);
	}
	
	public static boolean isRedirect(String path) {
		if(isEmpty(path)) return false;
		return path.startsWith("redirect:");
	}
}
