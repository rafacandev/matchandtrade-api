package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.Json;

public class AuthenticationJson implements Json {

	private Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}