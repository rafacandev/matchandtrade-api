package com.matchandtrade.rest.v1.link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.rest.v1.controller.ItemController;
import com.matchandtrade.rest.v1.controller.WantItemController;
import com.matchandtrade.rest.v1.json.WantItemJson;

public class WantItemLinkAssember {
	
	// Utility classes, which are a collection of static members, are not meant to be instantiated. Hence, at least one non-public constructor should be defined.
	private WantItemLinkAssember() {}

	public static void assemble(WantItemJson json, Integer itemId, Integer tradeMembershipId) {
		if (json != null && json.getWantItemId() != null) {
			Link selfLink = linkTo(methodOn(WantItemController.class).get(tradeMembershipId, itemId, json.getItemId())).withSelfRel();
			json.getLinks().add(selfLink);
			if (json.getItemId() != null) {
				Link itemLink = linkTo(methodOn(ItemController.class).get(tradeMembershipId, json.getItemId())).withRel("item");
				json.getLinks().add(itemLink);
			}
		}
	}

	public static void assemble(SearchResult<WantItemJson> searchResult, Integer itemId, Integer tradeMembershipId) {
		searchResult.getResultList().forEach(j -> assemble(j, itemId, tradeMembershipId));
	}

}
