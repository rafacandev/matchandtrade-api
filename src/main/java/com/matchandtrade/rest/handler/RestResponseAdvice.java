package com.matchandtrade.rest.handler;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.matchandtrade.common.SearchResult;
import com.matchandtrade.rest.Json;
import com.matchandtrade.rest.JsonLinkSupport;

@ControllerAdvice
public class RestResponseAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	/**
	 * When <i>body</i> is an instance of <i>JsonLinkSupport<i> or SearchResult<JsonLinkSupport>, then
	 * load the HATEOAS links of each JsonLinkSupport object.
	 * 
	 * Additionally, sets status code as HttpStatus.NOT_FOUND of body is null or SearchResult.getResultList().isEmpty().
	 * 
	 * @see http://projects.spring.io/spring-hateoas/
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object beforeBodyWrite(
				Object body,
				MethodParameter returnType,
				MediaType selectedContentType,
				Class<? extends HttpMessageConverter<?>> selectedConverterType,
				ServerHttpRequest request,
				ServerHttpResponse response) {

		// If body is null, then return HttpStatus.NOT_FOUND
		if (body == null) {
			response.setStatusCode(HttpStatus.NOT_FOUND);
			return null;
		}
		
		// If is a JsonLinkSupport, then build its links using Spring HATEOAS. See: http://projects.spring.io/spring-hateoas/
		if (body instanceof JsonLinkSupport) {
			((JsonLinkSupport) body).buildLinks();
			return body;
		}
		
		/*
		 * SearchResult is going to be serialized as an JSON array.
		 * Also, build SearchResult.getResultList() links using Spring HATEOAS. See: http://projects.spring.io/spring-hateoas/
		 * Also, is assumed that SearchResult should only be returned by Controller classes and are SearchResult<Json>. 
		 */
		// 
		if (body instanceof SearchResult) {
			handleSearchResult((SearchResult) body, response);
			
			// Handle pagination
			
			return body;
			
		}
		return body;
	}

	private void handleSearchResult(SearchResult<Json> searchResult, ServerHttpResponse response) {
		if (searchResult.getResultList().isEmpty()) {
			response.setStatusCode(HttpStatus.NOT_FOUND);
		} else {
			for (Json j : searchResult.getResultList()) {
				if (j instanceof JsonLinkSupport) {
					JsonLinkSupport jAsJsonLinkSupport = (JsonLinkSupport) j;
					jAsJsonLinkSupport.buildLinks();
				}
			}
		}
	}


}