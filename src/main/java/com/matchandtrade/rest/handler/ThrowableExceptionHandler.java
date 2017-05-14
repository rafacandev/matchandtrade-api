package com.matchandtrade.rest.handler;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.matchandtrade.rest.RestException;

@ControllerAdvice
public class ThrowableExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ThrowableExceptionHandler.class);

	@ExceptionHandler(Throwable.class)
    @ResponseBody
    ResponseEntity<Object> handleControllerException(HttpServletRequest request, Throwable exception) {
		// Default is null, which results in no response body
		Object responseEntityObject = null;
		
		// Default is http status 500
    	HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    	if (exception instanceof RestException) {
    		RestException e = (RestException) exception;
    		status = e.getHttpStatus();
    		if (e.getDescription() != null) {
        		responseEntityObject = buildRestErrorJson(e);
			}
		} else {
			LOGGER.error("Error proccessing request to URI: [{}]. Exception message: [{}].", request.getRequestURI(), exception.getMessage(), exception);
			RestErrorJson restErrorJson = new RestErrorJson();
			restErrorJson.setDescription("Unknown error. " + status.getReasonPhrase());
			responseEntityObject = restErrorJson;
		}
    	return new ResponseEntity<>(responseEntityObject, status);
    }

	private Object buildRestErrorJson(Exception e) {
		Object result;
		RestErrorJson restErrorJson = new RestErrorJson();
		restErrorJson.setDescription(e.getMessage());
		result = restErrorJson;
		return result;
	}

}