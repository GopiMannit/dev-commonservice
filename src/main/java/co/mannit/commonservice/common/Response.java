package co.mannit.commonservice.common;

import lombok.Data;

@Data
public class Response<T> {

	private String message;
	private int statusCode;
	private T source;
	
	
	
	public static <E> Response<E> buildSuccessMsg(int statusCode, String message, E source) {
		Response<E> response = new Response<>();
		response.setStatusCode(statusCode);
		response.setMessage(message);
		response.setSource(source);
		return response;
	}
}
