package com.matchandtrade.rest.handler;

import com.matchandtrade.rest.RestException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

public class ThrowableExceptionHandlerUT extends ResponseEntityExceptionHandler {

	private ThrowableExceptionHandler fixture;

	@Before
	public void before() {
		fixture = new ThrowableExceptionHandler();
	}

	@Test
	public void handleControllerException_When_ExceptionIsRestExceptionAndDoesNotHaveDescription_Then_ResponseStatusIsPreservedAndDescriptionIsEqualToStatusReasonPhrase() {
		HttpServletRequest request = new MockHttpServletRequest("GET", "http://test.com");
		RestException e = new RestException(HttpStatus.FORBIDDEN);

		ResponseEntity<Object> response = fixture.handleControllerException(request, e);
		Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		RestErrorJson restErrorJson = (RestErrorJson) response.getBody();
		Assert.assertEquals(HttpStatus.FORBIDDEN.getReasonPhrase(), restErrorJson.getDescription());
	}

	@Test
	public void handleControllerException_When_ExceptionIsRestExceptionAndItHasDescription_Then_ResponseStatusAndDescriptionArePreserved() {
		HttpServletRequest request = new MockHttpServletRequest("GET", "http://test.com");
		RestException e = new RestException(HttpStatus.ALREADY_REPORTED, "Exception message");

		ResponseEntity<Object> response = fixture.handleControllerException(request, e);
		Assert.assertEquals(HttpStatus.ALREADY_REPORTED, response.getStatusCode());
		RestErrorJson restErrorJson = (RestErrorJson) response.getBody();
		Assert.assertEquals(e.getDescription(), restErrorJson.getDescription());
	}
	
	@Test
	public void handleControllerException_When_ExceptionIsRuntimeException_Then_StatusIsInternalServerError() {
		HttpServletRequest request = new MockHttpServletRequest("GET", "http://test.com");
		RuntimeException e = new RuntimeException("TEST EXCEPTION");

		ResponseEntity<Object> response = fixture.handleControllerException(request, e);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

}