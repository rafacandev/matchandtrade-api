package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.JsonLinkSupport;

public class AuthenticationJson extends JsonLinkSupport {

	private Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}