package com.matchandtrade.rest.v1.link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.matchandtrade.common.SearchResult;
import com.matchandtrade.rest.v1.controller.TradeController;
import com.matchandtrade.rest.v1.json.TradeJson;

public class TradeLinkAssember {
	
	/*
	 * Utility classes, which are a collection of static members, are not meant to be instantiated.
	 * Hence, at least one non-public constructor should be defined.
	 */
	private TradeLinkAssember() {}

	public static void assemble(TradeJson json) {
		if (json != null) {
			json.getLinks().add(linkTo(methodOn(TradeController.class).get(json.getTradeId())).withSelfRel());
		}
	}

	public static void assemble(SearchResult<TradeJson> response) {
		for (TradeJson i : response.getResultList()) {
			assemble(i);
		}
	}

}
