package com.matchandtrade;


import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.config.RestResponseAdvice;
import com.matchandtrade.rest.JsonResponse;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class RestResponseInterceptorUT {
	
	@Test
	public void beforeBodyWritePositive() throws URISyntaxException {
		// Mock
		ServerHttpRequest mockRequest = Mockito.mock(ServerHttpRequest.class);
		Mockito.when(mockRequest.getURI()).thenReturn(new URI("http://localhost/RestResponseInterceptorUT"));
		
		// Testing input
		UserJson body = UserRandom.next(1);
		RestResponseAdvice interceptor = new RestResponseAdvice();
		JsonResponse jsonResponse = (JsonResponse) interceptor.beforeBodyWrite(body, null, null, null, mockRequest, null);
		
		// Assert the response
		UserJson bodyResponseAsJson = (UserJson) jsonResponse.getData();
		assertEquals("http://localhost/rest/v1/users/" + body.getUserId(), bodyResponseAsJson.getId().getHref());
		
	}

}
