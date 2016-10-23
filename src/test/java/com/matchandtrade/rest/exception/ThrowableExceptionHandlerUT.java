package com.matchandtrade.rest.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.authorization.AuthorizationException.Type;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.rest.exception.ThrowableExceptionHandler.ErrorJson;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ThrowableExceptionHandlerUT extends ResponseEntityExceptionHandler {

	@Test
	public void forbidden() {
		ThrowableExceptionHandler throwableExceptionHandler = new ThrowableExceptionHandler();
		AuthorizationException e = new AuthorizationException(Type.FORBIDDEN);
		ResponseEntity<ErrorJson> response = throwableExceptionHandler.handleControllerException(null, e);
		
		Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		Assert.assertEquals("403", response.getBody().getErrors().iterator().next().getKey());
	}
	
	@Test
	public void unauthorized() {
		ThrowableExceptionHandler throwableExceptionHandler = new ThrowableExceptionHandler();
		AuthorizationException e = new AuthorizationException(Type.UNAUTHORIZED);
		ResponseEntity<ErrorJson> response = throwableExceptionHandler.handleControllerException(null, e);
		
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		Assert.assertEquals("401", response.getBody().getErrors().iterator().next().getKey());
	}
	
	@Test
	public void restExceptionBadGateway() {
		ThrowableExceptionHandler throwableExceptionHandler = new ThrowableExceptionHandler();
		RestException e = new RestException(HttpStatus.BAD_GATEWAY);
		ResponseEntity<ErrorJson> response = throwableExceptionHandler.handleControllerException(null, e);
		
		Assert.assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
		Assert.assertEquals("502", response.getBody().getErrors().iterator().next().getKey());
	}
	
	@Test
	public void restExceptionMultipleErrors() {
		ThrowableExceptionHandler throwableExceptionHandler = new ThrowableExceptionHandler();
		RestException e = new RestException(HttpStatus.CONFLICT, "firstErrorKey", "firstErrorMessage");
		e.getErrors().put("secondErrorKey", "secondErrorMessage");
		ResponseEntity<ErrorJson> response = throwableExceptionHandler.handleControllerException(null, e);
		
		Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		Assert.assertEquals(2, response.getBody().getErrors().size());
	}

}