package com.matchandtrade.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ResponseEntity<ErrorJson> handleControllerException(HttpServletRequest request, Throwable exception) {
    	ErrorJson errorJson = new ErrorJson();
    	HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    	if (exception instanceof RestException) {
    		RestException e = (RestException) exception;
    		status = e.getHttpStatus();
    		e.getErrors().forEach( (k, v) -> {
    			errorJson.getErrors().put(k, v);
    		});
		} else {
			errorJson.getErrors().put(exception.getClass().getSimpleName(), exception.getMessage());
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
    	private Map<String, String> errors = new HashMap<>();

		public Map<String, String> getErrors() {
			return errors;
		}
    }

}