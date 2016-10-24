package com.matchandtrade.rest;

/**
 * Base for all JSON responses which includes: requestURL, data and pagination.
 * 
 * @author rafael.santos.bra@gmail.com
 * @see com.matchandtrade.config.RestResponseInterceptor;
 */
public class JsonResponse {
	private Json data;
	private String requestURL;

	public Json getData() {
		return data;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setData(Json data) {
		this.data = data;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}
	 
}
