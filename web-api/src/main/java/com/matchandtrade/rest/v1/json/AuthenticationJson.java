package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.Json;

public class AuthenticationJson implements Json {

	private Integer authenticationId;
	private Integer userId;
	private String token;

	public Integer getAuthenticationId() {
		return authenticationId;
	}

	public String getToken() {
		return token;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setAuthenticationId(Integer authenticationId) {
		this.authenticationId = authenticationId;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}