package com.matchandtrade.rest.handler;

public class RestError {

	private String key;
	private String description;

	public RestError(String key, String description) {
			this.key = key;
			this.description = description;
		}

	public String getKey() {
		return key;
	}

	public String getDescription() {
		return description;
	}

}