package com.matchandtrade.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAuthentication {

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

	@JsonProperty("isAuthenticated")
	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	@JsonProperty("isNewUser")
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
