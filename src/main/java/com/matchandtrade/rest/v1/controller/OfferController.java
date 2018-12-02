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
@RequestMapping(path = "/matchandtrade-api/v1/memberships")
public class OfferController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	OfferTransformer offerTransformer;
	@Autowired
	OfferService offerService;
	@Autowired
	OfferValidator offerValidator;

	@RequestMapping(path="/{membershipId}/offers/{offerId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("membershipId")Integer membershipId, @PathVariable("offerId")Integer offerId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		offerValidator.validateDelete(authenticationProvider.getAuthentication().getUser().getUserId(), membershipId, offerId);
		// Delegate to Service layer
		offerService.delete(offerId);
	}

	@RequestMapping(path="/{membershipId}/offers/{offerId}", method=RequestMethod.GET)
	public OfferJson get(@PathVariable("membershipId") Integer membershipId, @PathVariable("offerId") Integer offerId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		offerValidator.validateGetById(authenticationProvider.getAuthentication().getUser().getUserId(), membershipId);
		// Delegate to Service layer
		OfferEntity entity = offerService.findByOfferId(offerId);
		// Transform the response
		OfferJson response = offerTransformer.transform(entity);
//		// TODO: Assemble links
		return response;
	}

	@RequestMapping(path="/{membershipId}/offers", method=RequestMethod.GET)
	public SearchResult<OfferJson> get(@PathVariable("membershipId") Integer membershipId, Integer offeredArticleId, Integer wantedArticleId, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		offerValidator.validateGetAll(membershipId, _pageNumber, _pageSize, authenticationProvider.getAuthentication().getUser().getUserId());
		// Delegate to service layer
		SearchResult<OfferEntity> searchResult = offerService.findByMembershipIdOfferedArticleIdWantedArticleId(membershipId, offeredArticleId, wantedArticleId, _pageNumber, _pageSize);
		// Transform the response
		SearchResult<OfferJson> response = offerTransformer.transform(searchResult);
		// TODO: Assemble links
		return response;
	}
	
	@RequestMapping(path = "/{membershipId}/offers/", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public OfferJson post(@PathVariable Integer membershipId, @RequestBody OfferJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		offerValidator.validatePost(authenticationProvider.getAuthentication().getUser().getUserId(), membershipId, requestJson);
		// Transform the request
		OfferEntity entity = offerTransformer.transform(requestJson); 
		// Delegate to service layer
		offerService.create(membershipId, entity);
		// Transform the response
		OfferJson response = offerTransformer.transform(entity);
		// TODO: Assemble links
		return response;
	}

}
