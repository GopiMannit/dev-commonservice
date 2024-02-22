package co.mannit.commonservice.common;

import lombok.Data;

@Data
public class ErroResponse {

	private String errorCode;
	private String errorMsg;
	private String timeStamp;
	
	
}
