package com.matchandtrade.rest.v1.link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.Link;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.rest.v1.controller.ItemController;
import com.matchandtrade.rest.v1.controller.ItemAttachmentController;
import com.matchandtrade.rest.v1.json.ItemJson;

public class ItemLinkAssember {
	
	// Utility classes, which are a collection of static members, are not meant to be instantiated. Hence, at least one non-public constructor should be defined.
	private ItemLinkAssember() {}

	private static Set<Link> buildLink(Integer tradeMembershipId, Integer itemId) {
		Set<Link> result = new HashSet<>();
		result.add(linkTo(methodOn(ItemController.class).get(tradeMembershipId, itemId)).withSelfRel());
		result.add(linkTo(methodOn(ItemAttachmentController.class).get(tradeMembershipId, itemId, null, null)).withRel("files"));
		return result;
	}
	
	public static void assemble(ItemJson json, Integer tradeMembershipId) {
		if (json != null) {
			json.getLinks().addAll(buildLink(tradeMembershipId, json.getItemId()));
		}
	}

	public static void assemble(SearchResult<ItemJson> response, Integer tradeMembershipId) {
		for (ItemJson i : response.getResultList()) {
			assemble(i, tradeMembershipId);
		}
	}

}
