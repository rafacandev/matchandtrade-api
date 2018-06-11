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
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.OfferService;
import com.matchandtrade.rest.v1.json.OfferJson;
import com.matchandtrade.rest.v1.transformer.OfferTransformer;
import com.matchandtrade.rest.v1.validator.OfferValidator;

@RestController
@RequestMapping(path = "/matchandtrade-web-api/v1/trade-memberships")
public class OfferController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	OfferTransformer offerTransformer;
	@Autowired
	OfferService offerService;
	@Autowired
	OfferValidator offerValidator;

	@RequestMapping(path="/{tradeMembershipId}/offers/{offerId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("tradeMembershipId")Integer tradeMembershipId, @PathVariable("offerId")Integer offerId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		offerValidator.validateDelete(tradeMembershipId, offerId, authenticationProvider.getAuthentication().getUser().getUserId());
		// Delegate to Service layer
		offerService.delete(offerId);
	}

	@RequestMapping(path="/{tradeMembershipId}/offers/{offerId}", method=RequestMethod.GET)
	public OfferJson get(@PathVariable("tradeMembershipId") Integer tradeMembershipId, @PathVariable("offerId") Integer offerId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		offerValidator.validateGetById(tradeMembershipId, offerId, authenticationProvider.getAuthentication().getUser().getUserId());
		// Delegate to Service layer
		OfferEntity entity = offerService.get(offerId);
		// Transform the response
		OfferJson response = OfferTransformer.transform(entity);
//		// TODO: Assemble links
		return response;
	}

	@RequestMapping(path="/{tradeMembershipId}/offers", method=RequestMethod.GET)
	public SearchResult<OfferJson> get(@PathVariable("tradeMembershipId") Integer tradeMembershipId, Integer offeredItemId, Integer wantedItemId, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		offerValidator.validateGetAll(tradeMembershipId, offeredItemId, wantedItemId, _pageNumber, _pageSize, authenticationProvider.getAuthentication().getUser().getUserId());
		// Delegate to service layer
		SearchResult<OfferEntity> searchResult = offerService.search(tradeMembershipId, offeredItemId, wantedItemId, _pageNumber, _pageSize);
		// Transform the response
		SearchResult<OfferJson> response = OfferTransformer.transform(searchResult);
		// TODO: Assemble links
		return response;
	}
	
	@RequestMapping(path = "/{tradeMembershipId}/offers", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public OfferJson post(@PathVariable Integer tradeMembershipId, @RequestBody OfferJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		offerValidator.validatePost(tradeMembershipId, requestJson, authenticationProvider.getAuthentication().getUser().getUserId());
		// Transform the request
		OfferEntity entity = offerTransformer.transform(requestJson); 
		// Delegate to service layer
		offerService.create(tradeMembershipId, entity);
		// Transform the response
		OfferJson response = OfferTransformer.transform(entity);
		// TODO: Assemble links
		return response;
	}

}
