package com.matchandtrade;


import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.rest.handler.RestResponseAdvice;
import com.matchandtrade.rest.v1.json.JsonResponse;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class RestResponseInterceptorUT {
	
	@Test
	public void userJsonPositive() throws URISyntaxException {
		// Mock
		ServerHttpRequest mockRequest = Mockito.mock(ServerHttpRequest.class);
		Mockito.when(mockRequest.getURI()).thenReturn(new URI("http://localhost/"));
		
		// Testing input
		UserJson body = UserRandom.next(1);
		RestResponseAdvice interceptor = new RestResponseAdvice();
		JsonResponse jsonResponse = (JsonResponse) interceptor.beforeBodyWrite(body, null, null, null, mockRequest, null);
		
		// Assert the response
		UserJson bodyResponseAsJson = (UserJson) jsonResponse.getData();
		assertEquals("http://localhost/rest/v1/users/" + body.getUserId(), bodyResponseAsJson.getId().getHref());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void searchResultPositive() throws URISyntaxException {
		// Mock
		ServerHttpRequest mockRequest = Mockito.mock(ServerHttpRequest.class);
		Mockito.when(mockRequest.getURI()).thenReturn(new URI("http://localhost/"));
		
		// Testing input
		List<UserJson> resultList = new ArrayList<>();
		resultList.add(UserRandom.next(1));
		SearchResult<UserJson> body = new SearchResult<>(resultList, new Pagination());
		RestResponseAdvice interceptor = new RestResponseAdvice();
		JsonResponse jsonResponse = (JsonResponse) interceptor.beforeBodyWrite(body, null, null, null, mockRequest, null);
		UserJson bodyResponseAsJson = (UserJson) jsonResponse.getData();
		// Assert the response
		assertEquals(resultList.get(0).getEmail(), bodyResponseAsJson.getEmail());
		assertEquals("http://localhost/rest/v1/users/1", bodyResponseAsJson.getId().getHref());
	}

}
