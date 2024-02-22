package co.mannit.commonservice.common.util;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

	static final int SECONDS_PER_MINUTE = 60;
	
	public static String getCurrentDateTime() {
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss z");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
	
	public static int getTimeZoneOffsetinMinutes() {
		 TimeZone defaultTimeZone = TimeZone.getDefault();
		 ZoneOffset zoneOffset = ZoneId.of(defaultTimeZone.getID()).getRules().getOffset(java.time.Instant.now());
		 return zoneOffset.getTotalSeconds()/SECONDS_PER_MINUTE;
	}
	
	
	public static void main(String[] args) {
//		System.out.println(getCurrentDateTime());
//		System.out.println(getTimeZoneOffsetinMinutes());
	}
}
