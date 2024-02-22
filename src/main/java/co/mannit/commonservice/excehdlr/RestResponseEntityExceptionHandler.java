package co.mannit.commonservice.excehdlr;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import co.mannit.commonservice.ServiceCommonException;
import co.mannit.commonservice.common.ErroResponse;


@PropertySource("classpath:errormsg.properties")
@ControllerAdvice
public class RestResponseEntityExceptionHandler {

	private static final Logger logger = LogManager.getLogger(RestResponseEntityExceptionHandler.class);
			
//	@Autowired
//	private org.springframework.core.env.Environment environment;
	
	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(value = { Exception.class})
	protected ResponseEntity<ErroResponse> handleConflict(Exception ex, WebRequest request) {
		logger.debug("<handleConflict>");
	    ErroResponse errorResponse = new ErroResponse();
	    errorResponse.setErrorCode(null);
	    errorResponse.setErrorMsg(ex.getMessage());
	    errorResponse.setTimeStamp(LocalDateTime.now().toString());
	    logger.debug("</handleConflict>");
	    
	    logger.debug(stackTraceToString(ex));
	    
	    return new ResponseEntity<ErroResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    /*return handleExceptionInternal(ex, bodyOfResponse, 
	      new HttpHeaders(), HttpStatus.CONFLICT, request);*/
	}
	
	Map<Integer, BiFunction<String, String[], String>> msgFormatter = new HashMap<>();
	{
		msgFormatter.put(1, (msg, arg)->{
				return String.format(msg, arg[0]);
			});
		
		msgFormatter.put(2, (msg, arg)->{
				return String.format(msg, arg[0], arg[1]);
			});
		
		msgFormatter.put(3, (msg, arg)->{
				return String.format(msg, arg[0], arg[1], arg[2]);
			});
		
		msgFormatter.put(4, (msg, arg)->{
				return String.format(msg, arg[0], arg[1], arg[2], arg[3]);
			});
		
		msgFormatter.put(5, (msg, arg)->{
				return String.format(msg, arg[0], arg[1], arg[2], arg[3], arg[4]);
			});
	}
	
	@ExceptionHandler(value = { ServiceCommonException.class})
	protected ResponseEntity<ErroResponse> handleBusinessConflict(ServiceCommonException ex, WebRequest request) {
		logger.debug("<handleBusinessConflict>");
//		String message = environment.getProperty(ex.getErrorCode());
		String message = messageSource.getMessage(ex.getErrorCode(), null, null);
	    ErroResponse errorResponse = new ErroResponse();
	    errorResponse.setErrorCode(ex.getErrorCode());
	    
	    if(message==null) {
	    	errorResponse.setErrorMsg(ex.getMessage());
	    }else {
//	    	errorResponse.setErrorMsg(message==null?ex.getMessage():message);
	    	if(ex.getParams() == null || ex.getParams().length == 0) {
	    		errorResponse.setErrorMsg(message);
	    	}else {
	    		errorResponse.setErrorMsg(msgFormatter.get(ex.getParams().length).apply(message, ex.getParams()));
	    	}
	    }
	    
	    errorResponse.setTimeStamp(LocalDateTime.now().toString());
	    
	    logger.debug(stackTraceToString(ex));
	    logger.debug("</handleBusinessConflict> ErroResponse{}",errorResponse);
	    return new ResponseEntity<ErroResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public static String stackTraceToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
