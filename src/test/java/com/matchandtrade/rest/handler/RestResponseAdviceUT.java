package com.matchandtrade.rest.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
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
	
	private void buildServletHttpResponseForPagination(int pageNumber, int pageSize, long total) throws URISyntaxException {
		// Test targeting RestResponseAdvice.buildPaginationHeader()
		URI uri = new URIBuilder()
				.setScheme("http")
				.setHost("www.test.com")
				.setPath("/mypath")
				.setParameter("email", "myemail@mail.com")
				.setParameter("_pageSize", ""+pageSize)
				.setParameter("_pageNumber", ""+pageNumber)
				.build();
		// Mocking
		ServerHttpRequest request = Mockito.mock(ServerHttpRequest.class);
		Mockito.when(request.getURI()).thenReturn(uri);
		List<UserJson> resultList = new ArrayList<>();
		UserJson user1 = UserRandom.nextJson();
		user1.setUserId(1);
		resultList.add(user1);
		UserJson user2 = UserRandom.nextJson();
		user2.setUserId(2);
		resultList.add(user2);
		UserJson user3 = UserRandom.nextJson();
		user3.setUserId(3);
		resultList.add(user3);
		SearchResult<UserJson> body = new SearchResult<>(resultList, new Pagination(pageNumber, pageSize, total));
		// Execution
		RestResponseAdvice adivice = new RestResponseAdvice();
		adivice.beforeBodyWrite(body, null, null, null, request, serverHttpResponse);
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
		UserJson userJson = UserRandom.nextJson();
		userJson.setUserId(1);
		resultList.add(userJson);
		SearchResult<UserJson> body = new SearchResult<>(resultList, new Pagination(null, null));
		RestResponseAdvice adivice = new RestResponseAdvice();
		// Test execution
		List<UserJson> response = (List<UserJson>) adivice.beforeBodyWrite(body, null, null, null, serverHttpResquest, serverHttpResponse);
		UserJson responseUserJson = (UserJson) response.get(0);
		// Assertions
		assertEquals(resultList.get(0).getEmail(), responseUserJson.getEmail());
		assertNull(serverHttpResponse.getHeaders().get("Link"));
	}
	
	@Test
	public void paginationWithNextAndPrevious() throws URISyntaxException {
		buildServletHttpResponseForPagination(2,1,3L);
		// Assertions
		List<String> links = serverHttpResponse.getHeaders().get("Link");
		assertTrue(links.contains("<http://www.test.com/mypath?email=myemail%40mail.com&_pageSize=1&_pageNumber=3>; rel=\"nextPage\""));
		assertTrue(links.contains("<http://www.test.com/mypath?email=myemail%40mail.com&_pageSize=1&_pageNumber=1>; rel=\"previousPage\""));
		List<String> totalCount = serverHttpResponse.getHeaders().get("X-Pagination-Total-Count");
		assertEquals(1, totalCount.size());
		assertTrue(totalCount.contains("3"));
	}

	@Test
	public void paginationWithoutNextNorPrevious() throws URISyntaxException {
		buildServletHttpResponseForPagination(1,10,3L);
		// Assertions
		List<String> links = serverHttpResponse.getHeaders().get("Link");
		assertNull(links);
	}

	@Test
	public void paginationWithNextOnly() throws URISyntaxException {
		buildServletHttpResponseForPagination(1,2,3L);
		// Assertions
		List<String> links = serverHttpResponse.getHeaders().get("Link");
		assertEquals(1, links.size());
		assertTrue(links.contains("<http://www.test.com/mypath?email=myemail%40mail.com&_pageSize=2&_pageNumber=2>; rel=\"nextPage\""));
	}

	@Test
	public void paginationWithPreviousOnly() throws URISyntaxException {
		buildServletHttpResponseForPagination(2,2,3L);
		// Assertions
		List<String> links = serverHttpResponse.getHeaders().get("Link");
		assertEquals(1, links.size());
		assertTrue(links.contains("<http://www.test.com/mypath?email=myemail%40mail.com&_pageSize=2&_pageNumber=1>; rel=\"previousPage\""));
	}

}
