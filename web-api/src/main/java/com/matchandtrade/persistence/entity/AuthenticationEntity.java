package com.matchandtrade.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authentication")
public class AuthenticationEntity implements com.matchandtrade.persistence.entity.Entity {

	public enum Field {
		authenticationId, userId, antiForgeryState, token
	}

	private Integer authenticationId;
	private Integer userId;
	private String antiForgeryState;
	private String token;
	
	@Column(name = "anti_forgery_state", length = 500, nullable = true, unique = true)
	public String getAntiForgeryState() {
		return antiForgeryState;
	}
	
	@Id
	@Column(name = "authentication_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getAuthenticationId() {
		return authenticationId;
	}

	@Column(name = "token", length = 900, nullable = true, unique = false)
	public String getToken() {
		return token;
	}

	@Column(name = "user_id", nullable = true, unique = false)
	public Integer getUserId() {
		return userId;
	}

	public void setAntiForgeryState(String antiForgeryState) {
		this.antiForgeryState = antiForgeryState;
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