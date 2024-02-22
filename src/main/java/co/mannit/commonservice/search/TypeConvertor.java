package co.mannit.commonservice.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import co.mannit.commonservice.ServiceCommonException;
import co.mannit.commonservice.common.util.DateUtil;
import co.mannit.commonservice.common.util.TextUtil;

public class TypeConvertor {

	private static final Logger logger = LogManager.getLogger(TypeConvertor.class);
			
	static private Map<String, Function<String, ? extends Object>> dataTypeConverter = null;
	static {
		dataTypeConverter = new HashMap<>();
		dataTypeConverter.put("L", (t)-> Long.parseLong(t));
		dataTypeConverter.put("I", (t)-> Integer.parseInt(t));
		dataTypeConverter.put("S", (t)-> t);
		dataTypeConverter.put("D", (t)-> {
			SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date d = sd.parse(t);
				
				Calendar calendar = Calendar.getInstance();
			    calendar.setTime(d);
			    calendar.add(Calendar.MINUTE, DateUtil.getTimeZoneOffsetinMinutes());
			    
			    d = calendar.getTime();
			    logger.debug("Date : {}",d );
				return d;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		});
	}
	
	@SuppressWarnings("unchecked")
	static public <T> T convert(String type, String value) throws ServiceCommonException{
		
		if(TextUtil.isEmpty(String.valueOf(dataTypeConverter.get(type)))) throw new ServiceCommonException("110", new String[] {type});
		
		return (T) dataTypeConverter.get(type).apply(value);
	}
	
}
