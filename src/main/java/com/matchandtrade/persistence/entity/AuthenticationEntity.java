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
		authenticationId, userId, token
	}

	private Integer authenticationId;
	private Integer userId;
	private String token;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthenticationEntity other = (AuthenticationEntity) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Id
	@Column(name = "authentication_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getAuthenticationId() {
		return authenticationId;
	}

	@Column(name = "token", length = 500, nullable = false, unique = true)
	public String getToken() {
		return token;
	}

	@Column(name = "user_id", nullable = false, unique = false)
	public Integer getUserId() {
		return userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
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