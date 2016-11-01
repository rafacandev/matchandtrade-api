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
	@Override
	public Object beforeBodyWrite(
				Object body,
				MethodParameter returnType,
				MediaType selectedContentType,
				Class<? extends HttpMessageConverter<?>> selectedConverterType,
				ServerHttpRequest request,
				ServerHttpResponse response) {
		if (body == null) {
			response.setStatusCode(HttpStatus.NOT_FOUND);
			return null;
		}
		if (body instanceof JsonLinkSupport) {			
			// Load links using Spring HATEOAS. See: http://projects.spring.io/spring-hateoas/
			JsonLinkSupport bodyAsJsonLinkSupport = (JsonLinkSupport) body;
			bodyAsJsonLinkSupport.loadLinks();
			return bodyAsJsonLinkSupport;
		} else if (body instanceof SearchResult) {
			@SuppressWarnings("unchecked")
			SearchResult<Json> bodyAsSearchResult = (SearchResult<Json>) body;
			if (bodyAsSearchResult.getResultList().isEmpty()) {
				response.setStatusCode(HttpStatus.NOT_FOUND);
			} else {
				for (Json j : bodyAsSearchResult.getResultList()) {
					if (j instanceof JsonLinkSupport) {
						JsonLinkSupport jAsJsonLinkSupport = (JsonLinkSupport) j;
						jAsJsonLinkSupport.loadLinks();
					}
				}
			}
		}
		return body;
	}
}