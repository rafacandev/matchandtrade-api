package com.matchandtrade.rest.handler;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;
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
public class RestResponseAdiviceUT {
	
	private ServerHttpResponse serverHttpResponse = new ServletServerHttpResponse(new MockHttpServletResponse());
	private ServerHttpRequest serverHttpResquest = new ServletServerHttpRequest(new MockHttpServletRequest());
	
	@Test
	public void notFound(){
		// Test setup
		RestResponseAdvice adivice = new RestResponseAdvice();
		RestErrorJson response = (RestErrorJson) adivice.beforeBodyWrite(null, null, null, null, serverHttpResquest, serverHttpResponse);
		// Assert the response
		assertEquals(1, response.getErrors().size());
		ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) serverHttpResponse;
		assertEquals(404, servletResponse.getServletResponse().getStatus());
	}
	
	@Test
	public void userJsonNegative(){
		// Test setup
		UserJson body = UserRandom.nextJson(-1);
		RestResponseAdvice adivice = new RestResponseAdvice();
		UserJson response = (UserJson) adivice.beforeBodyWrite(body, null, null, null, serverHttpResquest, serverHttpResponse);
		// Assert the response
		assertEquals("http://localhost/rest/v1/users/" + body.getUserId(), response.getId().getHref());
	}

	@Test
	public void userJsonPositive(){
		// Test setup
		UserJson body = UserRandom.nextJson(1);
		RestResponseAdvice adivice = new RestResponseAdvice();
		UserJson response = (UserJson) adivice.beforeBodyWrite(body, null, null, null, serverHttpResquest, serverHttpResponse);
		// Assert the response
		assertEquals("http://localhost/rest/v1/users/" + body.getUserId(), response.getId().getHref());
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
		assertEquals("http://localhost/rest/v1/users/1", responseUserJson.getId().getHref());
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
		assertTrue(links.contains("<http://www.test.com/mypath?email=myemail%40mail.com&_pageNumber=3&_pageSize=5>; rel=\"nextPage\""));
		assertTrue(links.contains("<http://www.test.com/mypath?email=myemail%40mail.com&_pageNumber=1&_pageSize=5>; rel=\"previousPage\""));
		List<String> totalCount = serverHttpResponse.getHeaders().get("X-Pagination-Total-Count");
		assertEquals(1, totalCount.size());
		assertTrue(totalCount.contains("1"));
	}

}
