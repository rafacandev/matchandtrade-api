package com.matchandtrade.rest.v1.link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.matchandtrade.common.SearchResult;
import com.matchandtrade.rest.v1.controller.TradeMembershipController;
import com.matchandtrade.rest.v1.json.TradeMembershipJson;

public class TradeMembershipLinkAssember {
	
	/*
	 * Utility classes, which are a collection of static members, are not meant to be instantiated.
	 * Hence, at least one non-public constructor should be defined.
	 */
	private TradeMembershipLinkAssember() {}

	public static void assemble(TradeMembershipJson json) {
		if (json != null) {
			json.getLinks().add(linkTo(methodOn(TradeMembershipController.class).get(json.getTradeId())).withSelfRel());
		}
	}

	public static void assemble(SearchResult<TradeMembershipJson> response) {
		for (TradeMembershipJson i : response.getResultList()) {
			assemble(i);
		}
	}

}
