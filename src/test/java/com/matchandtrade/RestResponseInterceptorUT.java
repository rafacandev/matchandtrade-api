package com.matchandtrade;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.rest.handler.RestResponseAdvice;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.UserRandom;


@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class RestResponseInterceptorUT {
	
	private ServerHttpResponse httpResponse = new ServletServerHttpResponse(new MockHttpServletResponse());
	
	@Before
	public void before() throws URISyntaxException {
//		ServerHttpRequest mockRequest = Mockito.mock(ServerHttpRequest.class);
//		Mockito.when(mockRequest.getURI()).thenReturn(new URI("http://localhost/"));
	}

	@Test
	public void notFound(){
		// Test setup
		RestResponseAdvice interceptor = new RestResponseAdvice();
		Object response = interceptor.beforeBodyWrite(null, null, null, null, null, httpResponse);
		// Assert the response
		assertNull(response);
		ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) httpResponse;
		assertEquals(404, servletResponse.getServletResponse().getStatus());
	}
	
	@Test
	public void userJsonNegative(){
		// Test setup
		UserJson body = UserRandom.next(-1);
		RestResponseAdvice interceptor = new RestResponseAdvice();
		UserJson response = (UserJson) interceptor.beforeBodyWrite(body, null, null, null, null, httpResponse);
		// Assert the response
		assertEquals("http://localhost/rest/v1/users/" + body.getUserId(), response.getId().getHref());
	}

	@Test
	public void userJsonPositive(){
		// Test setup
		UserJson body = UserRandom.next(1);
		RestResponseAdvice interceptor = new RestResponseAdvice();
		UserJson response = (UserJson) interceptor.beforeBodyWrite(body, null, null, null, null, null);
		// Assert the response
		assertEquals("http://localhost/rest/v1/users/" + body.getUserId(), response.getId().getHref());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void searchResultNegative() {
		// Test setup
		List<UserJson> resultList = new ArrayList<>();
		SearchResult<UserJson> body = new SearchResult<>(resultList, new Pagination());
		RestResponseAdvice interceptor = new RestResponseAdvice();
		SearchResult<UserJson> response = (SearchResult<UserJson>) interceptor.beforeBodyWrite(body, null, null, null, null, httpResponse);
		assertTrue(response.getResultList().isEmpty());
		ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) httpResponse;
		assertEquals(404, servletResponse.getServletResponse().getStatus());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void searchResultPositive() {
		// Test setup
		List<UserJson> resultList = new ArrayList<>();
		resultList.add(UserRandom.next(1));
		SearchResult<UserJson> body = new SearchResult<>(resultList, new Pagination());
		RestResponseAdvice interceptor = new RestResponseAdvice();
		SearchResult<UserJson> response = (SearchResult<UserJson>) interceptor.beforeBodyWrite(body, null, null, null, null, null);
		UserJson responseUserJson = (UserJson) response.getResultList().get(0);
		// Assert the response
		assertEquals(resultList.get(0).getEmail(), responseUserJson.getEmail());
		assertEquals("http://localhost/rest/v1/users/1", responseUserJson.getId().getHref());
	}

}
