package com.matchandtrade.rest.exception;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.authorization.AuthorizationException.Type;

@ControllerAdvice
public class ThrowableExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ResponseEntity<ErrorJson> handleControllerException(HttpServletRequest request, Throwable exception) {
    	ErrorJson errorJson = new ErrorJson();
    	HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    	if (exception instanceof RestException) {
    		RestException e = (RestException) exception;
    		status = e.getHttpStatus();
    		e.getErrors().forEach( (k, v) -> {
    			errorJson.getErrors().add(new Error(k,v));
    		});
		} else if (exception instanceof AuthorizationException) {
			AuthorizationException e = (AuthorizationException) exception;
    		if (e.getType() == Type.UNAUTHORIZED) {
				status = HttpStatus.UNAUTHORIZED;
			} else if (e.getType() == Type.FORBIDDEN) {
				status = HttpStatus.FORBIDDEN;
			}
    		errorJson.getErrors().add(new Error(status.toString(), status.getReasonPhrase()));
		} else {
			errorJson.getErrors().add(new Error(status.toString(), status.getReasonPhrase()));
		}
    	
    	ResponseEntity<ErrorJson> entity = new ResponseEntity<>(errorJson, status);
    	return entity;
    }
    
    /**
     * Must contain getters even though is a private class.
     * Otherwise it won't be serialized as Json.
     * @author rafael.santos.bra@gmail.com
     */
    private class ErrorJson {
    	private List<Error> errors = new ArrayList<>();
		public List<Error> getErrors() {
			return errors;
		}
    }

    /**
     * Must contain getters even though is a private class.
     * Otherwise it won't be serialized as Json.
     * @author rafael.santos.bra@gmail.com
     */
    private class Error {
    	private String key;
    	private String description;
    	
    	public Error(String key, String description) {
    		this.key = key;
    		this.description = description;
    	}
    	
		@SuppressWarnings("unused")
		public String getKey() {
			return key;
		}
		@SuppressWarnings("unused")
		public String getDescription() {
			return description;
		}
    }

}