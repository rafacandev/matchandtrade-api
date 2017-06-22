package com.matchandtrade.rest.handler;

import java.net.URISyntaxException;
import java.nio.charset.Charset;
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

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.rest.Json;

/**
 * This <i>ResponseBodyAdvice</i> handles HATEOAS and response status codes.
 * It will build {@link Json} links and generate the header <i>Links</i> and <i>X-Pagination-Total-Count</i>
 * 
 * @see <a href="https://tools.ietf.org/html/rfc5988">https://tools.ietf.org/html/rfc5988</a>
 * @see <a href="https://developer.github.com/guides/traversing-with-pagination/">https://developer.github.com/guides/traversing-with-pagination/</a>
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
	 * @see <a href="http://projects.spring.io/spring-hateoas/">http://projects.spring.io/spring-hateoas/</a>
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
		
		logger.debug("Processing body instance of [{}] for URI [{}].", body.getClass(), request.getURI());
		/*
		 * SearchResult is going to be serialized as an JSON array.
		 * Also, build SearchResult.getResultList() links using Spring HATEOAS.
		 * Also, is assumed that SearchResult should only be returned by Controller classes and are SearchResult<Json>. 
		 */
		// 
		if (body instanceof SearchResult) {
			// Handle SearchResult
			SearchResult<Json> searchResult = (SearchResult) body;
			if (searchResult.getResultList().isEmpty()) {
				response.setStatusCode(HttpStatus.NOT_FOUND);
			}
			// Build pagination header
			PaginationHeader paginationHeader = buildPaginationHeader(request, searchResult);
			// Handle headers
			handlePaginationHeaders(response, paginationHeader);
			return searchResult.getResultList();
		}

		return body;
	}

	private PaginationHeader buildPaginationHeader(ServerHttpRequest request, SearchResult<Json> searchResult) {
		// URIBuilder for the root URI (URL without query params)
		URIBuilder rootUri = new URIBuilder()
				.setScheme(request.getURI().getScheme())
				.setHost(request.getURI().getHost())
				.setPort(request.getURI().getPort())
				.setPath(request.getURI().getPath());
		
		// Build query parameters without _pageSize and _pageNumber
		List<NameValuePair> queryParams = URLEncodedUtils.parse(request.getURI(), Charset.forName("UTF-8"));
		List<NameValuePair> queryParamsWithoutPaginationParameters = new ArrayList<>();
		for (NameValuePair param : queryParams) {
			if (!param.getName().equals(Pagination.Parameter.NUMBER.toString())) {
				queryParamsWithoutPaginationParameters.add(param);
			}
		}

		// Build next and previous page headers
		String nextPageHeader = null;
		String previousPageHeader = null;
		try {
			// need total and pageSize as double because 'int' does not offer enough precision
			double total = searchResult.getPagination().getTotal();
			double pageSize = searchResult.getPagination().getSize();
			// If has a next page
			if ((total / pageSize) > searchResult.getPagination().getNumber()) {
				URIBuilder nextPageUri = new URIBuilder(rootUri.build().toString());
				nextPageUri.addParameters(queryParamsWithoutPaginationParameters);
				nextPageUri.addParameter(Pagination.Parameter.NUMBER.toString(), String.valueOf(searchResult.getPagination().getNumber() + 1));
				nextPageHeader = "<" + nextPageUri.toString() + ">; rel=\"nextPage\"";
			}
			if (searchResult.getPagination().getNumber() > 1) {
				URIBuilder previousPageUri = new URIBuilder(rootUri.build().toString());
				previousPageUri.addParameters(queryParamsWithoutPaginationParameters);
				previousPageUri.addParameter(Pagination.Parameter.NUMBER.toString(), String.valueOf(searchResult.getPagination().getNumber() - 1));
				previousPageHeader = "<" + previousPageUri.toString() + ">; rel=\"previousPage\"";
			}
		} catch (URISyntaxException e) {
			logger.error("Unable to build pagination link header URI. Exception message: {}", e.getMessage(), e);
		}
		
		// Build the result
		PaginationHeader result = new PaginationHeader();
		String totalCount = String.valueOf(searchResult.getPagination().getTotal());
		result.totalCount = totalCount;
		result.nextPage = nextPageHeader;
		result.previousPage = previousPageHeader;
		return result;
	}

	private void handlePaginationHeaders(ServerHttpResponse response, PaginationHeader paginationHeader) {
		response.getHeaders().add("X-Pagination-Total-Count", paginationHeader.totalCount);
		if (paginationHeader.nextPage != null) {
			response.getHeaders().add("Link", paginationHeader.nextPage);
		}
		if (paginationHeader.previousPage != null) {
			response.getHeaders().add("Link", paginationHeader.previousPage);
		}
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

}