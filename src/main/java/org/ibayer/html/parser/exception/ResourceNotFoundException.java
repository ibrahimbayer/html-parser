package org.ibayer.html.parser.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5373697068977105760L;
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
	public ResourceNotFoundException(String message,Throwable cause) {
		super(message,cause);
	}
	
}
