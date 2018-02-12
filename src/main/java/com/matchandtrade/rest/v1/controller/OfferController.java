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
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.OfferService;
import com.matchandtrade.rest.v1.json.OfferJson;
import com.matchandtrade.rest.v1.transformer.OfferTransformer;
import com.matchandtrade.rest.v1.validator.OfferValidator;

@RestController
@RequestMapping(path = "/rest/v1/offers")
public class OfferController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	OfferTransformer offerTransformer;
	@Autowired
	OfferService offerService;
	@Autowired
	OfferValidator offerValidator;

	@RequestMapping(path = "/", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public OfferJson post(@RequestBody OfferJson json) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		offerValidator.validatePost(json, authenticationProvider.getAuthentication().getUser().getUserId());
		// Transform the request
		OfferEntity entity = offerTransformer.transform(json); 
		// Delegate to service layer
		offerService.create(entity);
		// Transform the response
		OfferJson response = OfferTransformer.transform(entity);
		// TODO: Assemble links
		return response;
	}

	@RequestMapping(path="/{offerId}", method=RequestMethod.GET)
	public OfferJson get(@PathVariable("offerId") Integer offerId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request - Nothing to validate
		// Delegate to Service layer
		OfferEntity entity = offerService.get(offerId);
//		// Transform the response
		OfferJson response = OfferTransformer.transform(entity);
//		// TODO: Assemble links
		return response;
	}

	public void delete(Integer offerId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		offerValidator.validateDelete(offerId, authenticationProvider.getAuthentication().getUser().getUserId());
	}
	

}
