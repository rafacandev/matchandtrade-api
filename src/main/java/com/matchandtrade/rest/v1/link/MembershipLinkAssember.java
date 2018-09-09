package com.matchandtrade.rest.v1.link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.rest.v1.controller.TradeController;
import com.matchandtrade.rest.v1.controller.MembershipController;
import com.matchandtrade.rest.v1.controller.UserController;
import com.matchandtrade.rest.v1.json.MembershipJson;

public class MembershipLinkAssember {
	
	// Utility classes, which are a collection of static members, are not meant to be instantiated. Hence, at least one non-public constructor should be defined.
	private MembershipLinkAssember() {}

	public static void assemble(MembershipJson json) {
		if (json != null) {
			json.getLinks().add(linkTo(methodOn(MembershipController.class).get(json.getMembershipId())).withSelfRel());
			json.getLinks().add(linkTo(methodOn(TradeController.class).get(json.getTradeId())).withRel("trade"));
			json.getLinks().add(linkTo(methodOn(UserController.class).get(json.getUserId())).withRel("user"));
		}
	}

	public static void assemble(SearchResult<MembershipJson> response) {
		for (MembershipJson i : response.getResultList()) {
			assemble(i);
		}
	}

}
