package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.ItemService;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.rest.v1.link.ItemLinkAssember;
import com.matchandtrade.rest.v1.transformer.ItemTransformer;
import com.matchandtrade.rest.v1.validator.ItemValidator;

@RestController
@RequestMapping(path = "/rest/v1/trade-memberships")
public class ItemController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	private ItemService itemService;
	@Autowired
	private ItemValidator itemValidator;

	@RequestMapping(path = "/{tradeMembershipId}/items", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public ItemJson post(@PathVariable Integer tradeMembershipId, @RequestBody ItemJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		itemValidator.validatePost(authenticationProvider.getAuthentication().getUser().getUserId(), tradeMembershipId, requestJson);
		// Transform the request
		ItemEntity itemEntity = ItemTransformer.transform(requestJson);
		// Delegate to service layer
		itemService.create(tradeMembershipId, itemEntity);
		// Transform the response
		ItemJson response = ItemTransformer.transform(itemEntity);
		// Assemble links
		ItemLinkAssember.assemble(response, tradeMembershipId);
		return response;
	}

	@RequestMapping(path = "/{tradeMembershipId}/items/{itemId}", method = RequestMethod.PUT)
	public ItemJson put(@PathVariable Integer tradeMembershipId, @PathVariable Integer itemId, @RequestBody ItemJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		requestJson.setItemId(itemId); // Always get the id from the URL when working on PUT methods
		itemValidator.validatePut(authenticationProvider.getAuthentication().getUser().getUserId(), tradeMembershipId, itemId, requestJson);
		// Transform the request
		ItemEntity itemEntity = ItemTransformer.transform(requestJson);
		// Delegate to service layer
		itemService.update(itemEntity);
		// Transform the response
		ItemJson response = ItemTransformer.transform(itemEntity);
		// Assemble links
		ItemLinkAssember.assemble(response, tradeMembershipId);
		return response;
	}

	@RequestMapping(path="/{tradeMembershipId}/items/{itemId}", method=RequestMethod.GET)
	public ItemJson get(@PathVariable("tradeMembershipId") Integer tradeMembershipId, @PathVariable("itemId") Integer itemId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		itemValidator.validateGet(authenticationProvider.getAuthentication().getUser().getUserId(), tradeMembershipId);
		// Delegate to service layer
		ItemEntity itemEntity = itemService.get(itemId);
		// Transform the response
		ItemJson response = ItemTransformer.transform(itemEntity);
		// Assemble links
		ItemLinkAssember.assemble(response, tradeMembershipId);
		return response;
	}

	@RequestMapping(path={"/{tradeMembershipId}/items/", "/{tradeMembershipId}/items"}, method=RequestMethod.GET)
	public SearchResult<ItemJson> get(@PathVariable("tradeMembershipId") Integer tradeMembershipId, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		itemValidator.validateGet(authenticationProvider.getAuthentication().getUser().getUserId(), tradeMembershipId, _pageNumber, _pageSize);
		// Delegate to service layer
		SearchResult<ItemEntity> searchResult = itemService.searchByTradeMembershipIdName(tradeMembershipId, _pageNumber, _pageSize);
		// Transform the response
		SearchResult<ItemJson> response = ItemTransformer.transform(searchResult);
		// Assemble links
		ItemLinkAssember.assemble(response, tradeMembershipId);
		return response;
	}
	
}
