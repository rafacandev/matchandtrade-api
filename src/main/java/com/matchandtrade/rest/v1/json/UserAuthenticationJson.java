package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.Json;

public class UserAuthenticationJson implements Json {
	private Integer userId;
	private boolean isAuthenticated = false;
	private Boolean isNewUser;
	private String email;
	private String name;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	public Boolean isNewUser() {
		return isNewUser;
	}

	public void setNewUser(Boolean isNewUser) {
		this.isNewUser = isNewUser;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
