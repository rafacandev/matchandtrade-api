package com.matchandtrade.rest.v1.link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.matchandtrade.rest.v1.controller.UserController;
import com.matchandtrade.rest.v1.json.UserJson;

public class UserLinkAssember {
	
	// Utility classes, which are a collection of static members, are not meant to be instantiated. Hence, at least one non-public constructor should be defined.
	private UserLinkAssember() {}

	public static void assemble(UserJson json) {
		if (json != null) {
			json.getLinks().add(linkTo(methodOn(UserController.class).get(json.getUserId())).withSelfRel());
		}
	}

}
