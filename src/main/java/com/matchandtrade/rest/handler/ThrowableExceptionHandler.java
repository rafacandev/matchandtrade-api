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

import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.authorization.AuthorizationException.Type;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.validator.ValidationException;

@ControllerAdvice
public class ThrowableExceptionHandler extends ResponseEntityExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(ThrowableExceptionHandler.class);

	@ExceptionHandler(Throwable.class)
    @ResponseBody
    ResponseEntity<RestErrorJson> handleControllerException(HttpServletRequest request, Throwable exception) {
    	RestErrorJson restErrorJson = new RestErrorJson();
    	HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    	if (exception instanceof RestException) {
    		RestException e = (RestException) exception;
    		status = e.getHttpStatus();
    		e.getErrors().forEach( (k, v) -> {
    			restErrorJson.getErrors().add(new RestError(k,v));
    		});
		} else if (exception instanceof AuthorizationException) {
			AuthorizationException e = (AuthorizationException) exception;
    		if (e.getType() == Type.UNAUTHORIZED) {
				status = HttpStatus.UNAUTHORIZED;
			} else if (e.getType() == Type.FORBIDDEN) {
				status = HttpStatus.FORBIDDEN;
			}
    		restErrorJson.getErrors().add(new RestError(status.toString(), status.getReasonPhrase()));
		} else if (exception instanceof ValidationException) {
			ValidationException e = (ValidationException) exception;
			status = HttpStatus.UNPROCESSABLE_ENTITY;
			e.getErrors().forEach( (k, v) -> 
				restErrorJson.getErrors().add(new RestError(k.toString(), v))
			);
		} else {
			logger.error("Error proccessing request to URI: [{}]. Exception message: [{}].", request.getRequestURI(), exception.getMessage(), exception);
			restErrorJson.getErrors().add(new RestError(status.toString(), status.getReasonPhrase()));
		}
    	
    	ResponseEntity<RestErrorJson> entity = new ResponseEntity<>(restErrorJson, status);
    	return entity;
    }
    

}