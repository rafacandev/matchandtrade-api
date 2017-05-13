package com.matchandtrade.rest.handler;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class RestResponseAdviceUT {
	
	private ServerHttpResponse serverHttpResponse;
	private ServerHttpRequest serverHttpResquest;
	
	@Before
	public void before() {
		serverHttpResponse = new ServletServerHttpResponse(new MockHttpServletResponse());
		serverHttpResquest = new ServletServerHttpRequest(new MockHttpServletRequest());
	}
	
	@Test
	public void bodyIsNull(){
		// Test setup
		RestResponseAdvice adivice = new RestResponseAdvice();
		adivice.beforeBodyWrite(null, null, null, null, serverHttpResquest, serverHttpResponse);
		// Assert the response
		ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) serverHttpResponse;
		assertEquals(404, servletResponse.getServletResponse().getStatus());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void searchResultNegative() {
		// Test setup
		List<UserJson> resultList = new ArrayList<>();
		SearchResult<UserJson> body = new SearchResult<>(resultList, new Pagination());
		RestResponseAdvice adivice = new RestResponseAdvice();
		List<UserJson> response = (List<UserJson>) adivice.beforeBodyWrite(body, null, null, null, serverHttpResquest, serverHttpResponse);
		assertTrue(response.isEmpty());
		ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) serverHttpResponse;
		assertEquals(404, servletResponse.getServletResponse().getStatus());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void searchResultPositive() {
		// Test setup
		List<UserJson> resultList = new ArrayList<>();
		resultList.add(UserRandom.nextJson(1));
		SearchResult<UserJson> body = new SearchResult<>(resultList, new Pagination());
		RestResponseAdvice adivice = new RestResponseAdvice();
		// Test execution
		List<UserJson> response = (List<UserJson>) adivice.beforeBodyWrite(body, null, null, null, serverHttpResquest, serverHttpResponse);
		UserJson responseUserJson = (UserJson) response.get(0);
		// Assertions
		assertEquals(resultList.get(0).getEmail(), responseUserJson.getEmail());
	}
	
	@Test
	public void paginationPositive() throws URISyntaxException {
		// Test setup
		URI uri = new URIBuilder()
	        .setScheme("http")
	        .setHost("www.test.com")
	        .setPath("/mypath")
	        .setParameter("email", "myemail@mail.com")
	        .setParameter("_pageSize", "5")
	        .setParameter("_pageNumber", "2")
	        .build();
		ServerHttpRequest request = Mockito.mock(ServerHttpRequest.class);
		Mockito.when(request.getURI()).thenReturn(uri);
		List<UserJson> resultList = new ArrayList<>();
		resultList.add(UserRandom.nextJson(1));
		SearchResult<UserJson> body = new SearchResult<>(resultList, new Pagination(2,5,1L));
		// Test execution
		RestResponseAdvice adivice = new RestResponseAdvice();
		adivice.beforeBodyWrite(body, null, null, null, request, serverHttpResponse);
		// Assertions
		List<String> links = serverHttpResponse.getHeaders().get("Link");
		assertTrue(links.contains("<http://www.test.com/mypath?email=myemail%40mail.com&_pageSize=5&_pageNumber=3>; rel=\"nextPage\""));
		assertTrue(links.contains("<http://www.test.com/mypath?email=myemail%40mail.com&_pageSize=5&_pageNumber=3>; rel=\"previousPage\""));
		List<String> totalCount = serverHttpResponse.getHeaders().get("X-Pagination-Total-Count");
		assertEquals(1, totalCount.size());
		assertTrue(totalCount.contains("1"));
	}
	
}
