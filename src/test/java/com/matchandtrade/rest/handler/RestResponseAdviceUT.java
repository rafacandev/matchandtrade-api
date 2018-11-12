package com.matchandtrade.rest.handler;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.random.UserRandom;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestResponseAdviceUT {

	private RestResponseAdvice fixture;

	private ServerHttpResponse serverHttpResponse;
	private ServerHttpRequest serverHttpResquest;

	@Before
	public void before() {
		serverHttpResponse = new ServletServerHttpResponse(new MockHttpServletResponse());
		serverHttpResquest = new ServletServerHttpRequest(new MockHttpServletRequest());
		fixture = new RestResponseAdvice();
	}

	@Test
	public void beforeBodyWrite_When_BodyIsNull_Then_ResponseStatusIs404() {
		// Test setup
		fixture.beforeBodyWrite(null, null, null, null, serverHttpResquest, serverHttpResponse);
		// Assert the response
		ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) serverHttpResponse;
		assertEquals(404, servletResponse.getServletResponse().getStatus());
	}
	
	private void invokeBeforeBodyWriteWithMockedRequestWhichGeneratesAPaginableResponse(int pageNumber, int pageSize, long total) throws URISyntaxException {
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

		ServerHttpRequest requestMock = mock(ServerHttpRequest.class);
		when(requestMock.getURI()).thenReturn(uri);
		List<UserJson> resultList = new ArrayList<>();
		UserJson user1 = UserRandom.createJson();
		user1.setUserId(1);
		resultList.add(user1);
		UserJson user2 = UserRandom.createJson();
		user2.setUserId(2);
		resultList.add(user2);
		UserJson user3 = UserRandom.createJson();
		user3.setUserId(3);
		resultList.add(user3);
		SearchResult<UserJson> body = new SearchResult<>(resultList, new Pagination(pageNumber, pageSize, total));
		// Execution
		RestResponseAdvice adivice = new RestResponseAdvice();
		adivice.beforeBodyWrite(body, null, null, null, requestMock, serverHttpResponse);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void beforeBodyWrite_When_BodyIsInstanceOfSearchResult_Then_ResponseIsAListAndResponseStatusIs200() {
		List<UserJson> resultList = new ArrayList<>();
		UserJson user = new UserJson();
		user.setUserId(1);
		user.setName("Test name");
		resultList.add(user);
		SearchResult<UserJson> body = new SearchResult<>(resultList, new Pagination());
		RestResponseAdvice fixture = new RestResponseAdvice();

		List<UserJson> response = (List<UserJson>) fixture.beforeBodyWrite(body, null, null, null, serverHttpResquest, serverHttpResponse);
		assertEquals(1, response.size());
		assertTrue(response.contains(user));
		ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) serverHttpResponse;
		assertEquals(200, servletResponse.getServletResponse().getStatus());
	}
	
	@Test
	public void beforeBodyWrite_When_PaginationHasNextAndPreviousPage_Then_ResponseContainsHeadersForNextPagePreviousPageAndTotalCount() throws URISyntaxException {
		invokeBeforeBodyWriteWithMockedRequestWhichGeneratesAPaginableResponse(2,1,3L);
		// Assertions
		List<String> links = serverHttpResponse.getHeaders().get("Link");
		assertTrue(links.contains("<http://www.test.com/mypath?email=myemail%40mail.com&_pageSize=1&_pageNumber=3>; rel=\"nextPage\""));
		assertTrue(links.contains("<http://www.test.com/mypath?email=myemail%40mail.com&_pageSize=1&_pageNumber=1>; rel=\"previousPage\""));
		List<String> totalCount = serverHttpResponse.getHeaders().get("X-Pagination-Total-Count");
		assertEquals(1, totalCount.size());
		assertTrue(totalCount.contains("3"));
	}

	@Test
	public void beforeBodyWrite_When_DoesNotHaveNextAndPreviousPage_Then_ResponseDoesNotContainHeadersForNextPagePreviousPage() throws URISyntaxException {
		invokeBeforeBodyWriteWithMockedRequestWhichGeneratesAPaginableResponse(1,10,3L);
		// Assertions
		List<String> links = serverHttpResponse.getHeaders().get("Link");
		assertNull(links);
	}

	@Test
	public void beforeBodyWrite_When_PaginationHasPreviousPage_Then_ResponseContainsHeaderForPreviousPage() throws URISyntaxException {
		invokeBeforeBodyWriteWithMockedRequestWhichGeneratesAPaginableResponse(1,2,3L);
		// Assertions
		List<String> links = serverHttpResponse.getHeaders().get("Link");
		assertEquals(1, links.size());
		assertTrue(links.contains("<http://www.test.com/mypath?email=myemail%40mail.com&_pageSize=2&_pageNumber=2>; rel=\"nextPage\""));
	}

}
