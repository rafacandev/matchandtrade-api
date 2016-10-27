package com.matchandtrade.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.matchandtrade.rest.Json;
import com.matchandtrade.rest.JsonLinkSuppport;
import com.matchandtrade.rest.JsonResponse;

@ControllerAdvice
public class RestResponseAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	/**
	 * When <i>body</i> is an instance of <i>Json<i>, then:
	 * Create an <i>JsonResponse<i> object and assing the <i>body</i> as <i>JsonResponse.data</i>
	 */
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
						Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
						ServerHttpResponse response) {
		// Add JSON meta-data if body is instance of Json.
		if (body instanceof Json) {
			if (body instanceof JsonLinkSuppport) {
				// Load links using Spring HATEOAS. See: http://projects.spring.io/spring-hateoas/
				JsonLinkSuppport bodyAsJsonLinkSupport = (JsonLinkSuppport) body;
				bodyAsJsonLinkSupport.loadLinks();
			}
			Json bodyAsJson = (Json) body;
			// Prepare JsonResponse
			JsonResponse jsonResponse = new JsonResponse();
			jsonResponse.setData(bodyAsJson);
			jsonResponse.setRequestURL(request.getURI().toString());
			return jsonResponse;
		}
		return body;
	}

}