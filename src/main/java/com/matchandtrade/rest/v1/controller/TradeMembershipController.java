package com.matchandtrade.rest.v1.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.Authorization;
import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.repository.TradeMembershipRepository;
import com.matchandtrade.rest.AuthenticationProvider;
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
	
	@RequestMapping(path="/", method=RequestMethod.POST)
	public TradeMembershipJson post(@RequestBody TradeMembershipJson requestJson) {
		// Validate request identity
		Authorization.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		tradeMembershipValidador.validatePost(requestJson);
		// Transform the request
		TradeMembershipEntity tradeEntity = tradeMembershipTransformer.transform(requestJson, false);
		// Delegate to Repository layer
		tradeMembershipRepository.save(tradeEntity);
		// Transform the response
		TradeMembershipJson result = TradeMembershipTransformer.transform(tradeEntity);
		return result;
	}
	
	@RequestMapping(path="/{tradeMembershipId}", method=RequestMethod.GET)
	public TradeMembershipJson get(@PathVariable("tradeMembershipId") Integer tradeMembershipId) {
		// Validate request identity
		Authorization.validateIdentity(authenticationProvider.getAuthentication());
		// Delegate to Repository layer
		TradeMembershipEntity searchResult = tradeMembershipRepository.get(tradeMembershipId);
		// Transform the response
		TradeMembershipJson result = TradeMembershipTransformer.transform(searchResult);
		return result;
	}
	
	@RequestMapping(path={"", "/"}, method=RequestMethod.GET)
	public SearchResult<TradeMembershipJson> get(Integer tradeId, Integer userId, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		Authorization.validateIdentity(authenticationProvider.getAuthentication());
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		if (userId != null) {
			searchCriteria.addCriterion(TradeMembershipEntity.Field.userId, userId);
		}
		if (tradeId != null) {
			searchCriteria.addCriterion(TradeMembershipEntity.Field.tradeId, tradeId);
		}
		
		// Delegate to Repository layer
		SearchResult<TradeMembershipEntity> searchResult = tradeMembershipRepository.search(searchCriteria);
		// Transform the response
		SearchResult<TradeMembershipJson> result = TradeMembershipTransformer.transform(searchResult);
		return result;
	}
	
}
