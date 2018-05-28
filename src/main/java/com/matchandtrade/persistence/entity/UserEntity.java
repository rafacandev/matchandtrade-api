package com.matchandtrade.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_tb") // 'user' is a reserved word in most databases, hence we are sufixing it with '_tb' 
public class UserEntity implements com.matchandtrade.persistence.entity.Entity {
	
	public enum Role {
		ADMINISTRATOR, USER
	}

	private Integer userId;
	private String email;
	private String name;
	private Role role = Role.USER;

	@Column(name = "email", length = 500, nullable = false, unique = true)
	public String getEmail() {
		return email;
	}

	@Column(name = "name", length = 150, nullable = false, unique = false)
	public String getName() {
		return name;
	}

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getUserId() {
		return userId;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="role", nullable=false, unique=false, length=100)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
}
