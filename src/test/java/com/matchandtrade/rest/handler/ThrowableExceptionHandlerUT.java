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
	public void restExceptionWithoutDescription() {
		HttpServletRequest request = new MockHttpServletRequest("GET", "http://localhost/ThrowableExceptionHandlerUT");
		ThrowableExceptionHandler throwableExceptionHandler = new ThrowableExceptionHandler();
		RestException e = new RestException(HttpStatus.FORBIDDEN);
		ResponseEntity<Object> response = throwableExceptionHandler.handleControllerException(request, e);
		Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		RestErrorJson restErrorJson = (RestErrorJson) response.getBody();
		Assert.assertEquals(HttpStatus.FORBIDDEN.getReasonPhrase(), restErrorJson.getDescription());
	}

	@Test
	public void restExceptionWithDescription() {
		HttpServletRequest request = new MockHttpServletRequest("GET", "http://localhost/ThrowableExceptionHandlerUT");
		ThrowableExceptionHandler throwableExceptionHandler = new ThrowableExceptionHandler();
		RestException e = new RestException(HttpStatus.ALREADY_REPORTED, "ThrowableExceptionHandlerUT.restExceptionWithDescription()");
		ResponseEntity<Object> response = throwableExceptionHandler.handleControllerException(request, e);
		Assert.assertEquals(HttpStatus.ALREADY_REPORTED, response.getStatusCode());
		RestErrorJson restErrorJson = (RestErrorJson) response.getBody();
		Assert.assertEquals(e.getDescription(), restErrorJson.getDescription());
	}
	
	@Test
	public void runtimeException() {
		HttpServletRequest request = new MockHttpServletRequest("GET", "http://localhost/ThrowableExceptionHandlerUT");
		ThrowableExceptionHandler throwableExceptionHandler = new ThrowableExceptionHandler();
		RuntimeException e = new RuntimeException("TEST EXCEPTION");
		ResponseEntity<Object> response = throwableExceptionHandler.handleControllerException(request, e);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
	
}