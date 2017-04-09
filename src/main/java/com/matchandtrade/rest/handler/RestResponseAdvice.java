package com.matchandtrade.rest.handler;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.rest.Json;
import com.matchandtrade.rest.JsonLinkSupport;

/**
 * This <i>ResponseBodyAdvice</i> handles HATEOAS and response status codes.
 * It will build {@link Json} links and generate the header <i>Links</i> and <i>X-Pagination-Total-Count</i>
 * 
 * @see https://tools.ietf.org/html/rfc5988
 * @see https://developer.github.com/guides/traversing-with-pagination/
 * @author rafael.santos.bra@gmail.com
 */
@ControllerAdvice
public class RestResponseAdvice implements ResponseBodyAdvice<Object> {
	
	private class PaginationHeader {
		public String previousPage;
		public String nextPage;
		public String totalCount;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(RestResponseAdvice.class);

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
			RestErrorJson errorJson = new RestErrorJson();
			errorJson.getErrors().add(new RestError(HttpStatus.NOT_FOUND.name(), "Did not find any resource for the URI: " + request.getURI()));
			return errorJson;
		}
		logger.debug("Processing body instance of [{}] for URI [{}].", body.getClass(), request.getURI());
		// If is a JsonLinkSupport, then build its links using Spring HATEOAS.
		if (body instanceof JsonLinkSupport) {
			((JsonLinkSupport) body).buildLinks();
			return body;
		}
		/*
		 * SearchResult is going to be serialized as an JSON array.
		 * Also, build SearchResult.getResultList() links using Spring HATEOAS.
		 * Also, is assumed that SearchResult should only be returned by Controller classes and are SearchResult<Json>. 
		 */
		// 
		if (body instanceof SearchResult) {
			// Handle SearchResult
			SearchResult<Json> searchResult = (SearchResult) body;
			handleSearchResult(searchResult, response);
			// Build pagination header
			PaginationHeader paginationHeader = buildPaginationHeader(request, searchResult);
			// Handle headers
			handlePaginationHeaders(response, paginationHeader);
			
			return searchResult.getResultList();
		}
		return body;
	}

	private PaginationHeader buildPaginationHeader(ServerHttpRequest request, SearchResult<Json> searchResult) {
		// URIBuilder for the basic URI (URL without query params)
		URIBuilder basicUriBuilder = new URIBuilder()
				.setScheme(request.getURI().getScheme())
				.setHost(request.getURI().getHost())
				.setPort(request.getURI().getPort())
				.setPath(request.getURI().getPath());
		// Rebuild query parameters without _pageSize and _pageNumber
		List<NameValuePair> queryParams = URLEncodedUtils.parse(request.getURI(), "UTF-8");
		for (NameValuePair param : queryParams) {
			if (!param.getName().equals(Pagination.Parameter.SIZE.toString()) && !param.getName().equals(Pagination.Parameter.NUMBER.toString())) {
				basicUriBuilder.addParameter(param.getName(), param.getValue());
			}
		}
		// Rebuild the URI without _pageSize and _pageNumber
		String basicUriString = null;
		try {
			basicUriString = basicUriBuilder.build().toString();
		} catch (URISyntaxException e) {
			logger.warn("Unable to build URI.", e);
		}
		// Build nextPage, previousPage query strings
		String paginationStartString = "<" + basicUriString + (queryParams.isEmpty() ? "" : "&") + Pagination.Parameter.NUMBER + "="; 
		String nextPage = paginationStartString 
				+ (searchResult.getPagination().getNumber() + 1)
				+ "&_pageSize=" + (searchResult.getPagination().getSize())
				+ ">; rel=\"nextPage\"";
		String previousPage = paginationStartString 
				+ (searchResult.getPagination().getNumber() - 1)
				+ "&_pageSize=" + (searchResult.getPagination().getSize())
				+ ">; rel=\"previousPage\"";
		// Build the result
		PaginationHeader result = new PaginationHeader();
		String totalCount = String.valueOf(searchResult.getPagination().getTotal());
		result.totalCount = totalCount;
		result.nextPage = nextPage;
		result.previousPage = previousPage;
		return result;
	}

	private void handlePaginationHeaders(ServerHttpResponse response, PaginationHeader paginationHeader) {
		response.getHeaders().add("X-Pagination-Total-Count", paginationHeader.totalCount);
		response.getHeaders().add("Link", paginationHeader.nextPage);
		response.getHeaders().add("Link", paginationHeader.previousPage);
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

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

}