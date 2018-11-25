package com.matchandtrade.persistence.entity;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "authentication")
public class AuthenticationEntity implements com.matchandtrade.persistence.entity.Entity {

	private Integer authenticationId;
	private String antiForgeryState;
	private String token;
	private UserEntity user;
	
	@ManyToOne
	@JoinColumn(name="user_id", foreignKey = @ForeignKey(name = "authentication_user_id_fk"))
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@Column(name = "anti_forgery_state", length = 500, nullable = true, unique = true)
	public String getAntiForgeryState() {
		return antiForgeryState;
	}
	
	@Id
	@Column(name = "authentication_id")
	@SequenceGenerator(name="authentication_id_generator", sequenceName = "authentication_id_sequence")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "authentication_id_generator")
	public Integer getAuthenticationId() {
		return authenticationId;
	}

	@Column(name = "token", length = 900, nullable = true, unique = false)
	public String getToken() {
		return token;
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

}