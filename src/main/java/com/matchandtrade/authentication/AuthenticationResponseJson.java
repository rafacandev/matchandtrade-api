package com.matchandtrade.authentication;

public class AuthenticationResponseJson {

	private Integer userId;
	private Boolean isNewUser;
	private String email;
	private String name;
	private String authorizationToken;
	

	public String getAuthorizationToken() {
		return authorizationToken;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public Integer getUserId() {
		return userId;
	}

	public Boolean isNewUser() {
		return isNewUser;
	}

	public void setAuthorizationToken(String authorizationToken) {
		this.authorizationToken = authorizationToken;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNewUser(Boolean isNewUser) {
		this.isNewUser = isNewUser;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
