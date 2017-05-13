package com.matchandtrade.rest.v1.link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.matchandtrade.rest.v1.controller.AuthenticationController;
import com.matchandtrade.rest.v1.json.AuthenticationJson;

public class AuthenticationLinkAssember {
	
	/*
	 * Utility classes, which are a collection of static members, are not meant to be instantiated.
	 * Hence, at least one non-public constructor should be defined.
	 */
	private AuthenticationLinkAssember() {}

	public static void assemble(AuthenticationJson json) {
		if (json != null) {
			json.getLinks().add(linkTo(methodOn(AuthenticationController.class).get()).withSelfRel());
		}
	}

}
