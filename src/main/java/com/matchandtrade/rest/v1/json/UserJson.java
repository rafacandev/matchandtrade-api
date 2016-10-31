package com.matchandtrade.rest.v1.json;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.matchandtrade.rest.JsonLinkSuppport;
import com.matchandtrade.rest.v1.controller.UserController;

public class UserJson extends JsonLinkSuppport {

	private String email;
	private String name;
	private Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public void loadLinks() {
		if (getUserId() != null) {
			add(linkTo(methodOn(UserController.class).getByUserId(getUserId())).withSelfRel());
		}
	}
}
