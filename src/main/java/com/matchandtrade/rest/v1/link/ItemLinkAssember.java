package com.matchandtrade.rest.v1.link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.rest.v1.controller.ItemController;
import com.matchandtrade.rest.v1.json.ItemJson;

public class ItemLinkAssember {
	
	// Utility classes, which are a collection of static members, are not meant to be instantiated. Hence, at least one non-public constructor should be defined.
	private ItemLinkAssember() {}

	public static Link buildLink(Integer tradeMembershipId, Integer itemId) {
		return linkTo(methodOn(ItemController.class).get(tradeMembershipId, itemId)).withSelfRel();
	}
	
	public static void assemble(ItemJson json, Integer tradeMembershipId) {
		if (json != null) {
			json.getLinks().add(buildLink(tradeMembershipId, json.getItemId()));
		}
	}

	public static void assemble(SearchResult<ItemJson> response, Integer tradeMembershipId) {
		for (ItemJson i : response.getResultList()) {
			assemble(i, tradeMembershipId);
		}
	}

}
