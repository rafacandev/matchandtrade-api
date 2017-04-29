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
import com.matchandtrade.rest.v1.validator.ValidationException;

@ControllerAdvice
public class ThrowableExceptionHandler extends ResponseEntityExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(ThrowableExceptionHandler.class);

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
		} else if (exception instanceof ValidationException) {
			ValidationException e = (ValidationException) exception;
			if (e.getErrorType() == ValidationException.ErrorType.RESOURCE_NOT_FOUND) {
				status = HttpStatus.NOT_FOUND;
			} else {
				status = HttpStatus.BAD_REQUEST;
			}
			responseEntityObject = buildRestErrorJson(e);
		} else {
			logger.error("Error proccessing request to URI: [{}]. Exception message: [{}].", request.getRequestURI(), exception.getMessage(), exception);
			RestErrorJson restErrorJson = new RestErrorJson();
			restErrorJson.setMessage("Unknown error. " + status.getReasonPhrase());
			responseEntityObject = restErrorJson;
		}
    	ResponseEntity<Object> entity = new ResponseEntity<>(responseEntityObject, status);
    	return entity;
    }

	private Object buildRestErrorJson(Exception e) {
		Object responseEntityObject;
		RestErrorJson restErrorJson = new RestErrorJson();
		restErrorJson.setMessage(e.getMessage());
		responseEntityObject = restErrorJson;
		return responseEntityObject;
	}

}