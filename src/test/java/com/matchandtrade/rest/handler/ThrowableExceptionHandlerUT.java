package com.matchandtrade.rest.handler;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.matchandtrade.rest.RestException;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ThrowableExceptionHandlerUT extends ResponseEntityExceptionHandler {
	
	@Test
	public void restException() {
		HttpServletRequest request = new MockHttpServletRequest("GET", "http://localhost/ThrowableExceptionHandlerUT");
		ThrowableExceptionHandler throwableExceptionHandler = new ThrowableExceptionHandler();
		RestException e = new RestException(HttpStatus.FORBIDDEN);
		ResponseEntity<Object> response = throwableExceptionHandler.handleControllerException(request, e);
		Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}
	
	@Test
	public void runtimeException() {
		HttpServletRequest request = new MockHttpServletRequest("GET", "http://localhost/ThrowableExceptionHandlerUT");
		ThrowableExceptionHandler throwableExceptionHandler = new ThrowableExceptionHandler();
		RuntimeException e = new RuntimeException("TEST EXCEPTION");
		ResponseEntity<Object> response = throwableExceptionHandler.handleControllerException(request, e);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
	
	@Test
	public void restExceptionMultipleErrors() {
		ThrowableExceptionHandler throwableExceptionHandler = new ThrowableExceptionHandler();
		RestException e = new RestException(HttpStatus.CONFLICT);
		ResponseEntity<Object> response = throwableExceptionHandler.handleControllerException(null, e);
		Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}
	
}