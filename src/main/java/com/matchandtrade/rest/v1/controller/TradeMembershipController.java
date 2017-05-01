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
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.repository.TradeMembershipRepository;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.TradeMembershipService;
import com.matchandtrade.rest.v1.json.TradeMembershipJson;
import com.matchandtrade.rest.v1.transformer.TradeMembershipTransformer;
import com.matchandtrade.rest.v1.validator.TradeMembershipValidator;

@RestController
@RequestMapping(path="/rest/v1/trade-memberships")
public class TradeMembershipController {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	TradeMembershipRepository tradeMembershipRepository;
	@Autowired
	TradeMembershipValidator tradeMembershipValidador;
	@Autowired
	TradeMembershipTransformer tradeMembershipTransformer;
	@Autowired
	TradeMembershipService tradeMembershipService;
	
	@RequestMapping(path="/", method=RequestMethod.POST)
	public TradeMembershipJson post(@RequestBody TradeMembershipJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		tradeMembershipValidador.validatePost(requestJson);
		// Transform the request
		TradeMembershipEntity tradeMembershipEntity = tradeMembershipTransformer.transform(requestJson);
		// Delegate to Service layer
		tradeMembershipService.create(tradeMembershipEntity);
		// Transform the response
		TradeMembershipJson result = TradeMembershipTransformer.transform(tradeMembershipEntity);
		return result;
	}
	
	@RequestMapping(path="/{tradeMembershipId}", method=RequestMethod.GET)
	public TradeMembershipJson get(@PathVariable("tradeMembershipId") Integer tradeMembershipId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Delegate to Service layer
		TradeMembershipEntity searchResult = tradeMembershipService.get(tradeMembershipId);
		// Transform the response
		TradeMembershipJson result = TradeMembershipTransformer.transform(searchResult);
		return result;
	}
	
	@RequestMapping(path={"", "/"}, method=RequestMethod.GET)
	public SearchResult<TradeMembershipJson> get(Integer tradeId, Integer userId, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Delegate to Service layer
		SearchResult<TradeMembershipEntity> searchResult = tradeMembershipService.search(tradeId, userId, _pageNumber, _pageSize);
		// Transform the response
		SearchResult<TradeMembershipJson> result = TradeMembershipTransformer.transform(searchResult);
		return result;
	}
	
	@RequestMapping(path="/{tradeMembershipId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer tradeMembershipId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		tradeMembershipValidador.validateDelete(tradeMembershipId);
		// Delegate to Service layer
		tradeMembershipService.delete(tradeMembershipId);
	}
	
}
