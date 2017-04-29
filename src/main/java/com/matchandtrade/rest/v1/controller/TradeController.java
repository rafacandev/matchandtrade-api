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
import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.repository.TradeMembershipRepository;
import com.matchandtrade.repository.TradeRepository;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;
import com.matchandtrade.rest.v1.validator.TradeValidator;

@RestController
@RequestMapping(path="/rest/v1/trades")
public class TradeController {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	TradeRepository tradeRepository;
	@Autowired
	TradeValidator tradeValidador;
	@Autowired
	TradeTransformer tradeTransformer;
	@Autowired
	TradeMembershipRepository tradeMembershipRepository;

	@RequestMapping(path="/", method=RequestMethod.POST)
	public TradeJson post(@RequestBody TradeJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		tradeValidador.validatePost(requestJson);
		// Transform the request
		TradeEntity tradeEntity = tradeTransformer.transform(requestJson, false);
		// Delegate to Repository layer
		tradeRepository.save(tradeEntity);
		// Make authenticated user the owner of the trade
		TradeMembershipEntity tradeMembershipEntity = new TradeMembershipEntity();
		tradeMembershipEntity.setTrade(tradeEntity);
		tradeMembershipEntity.setUser(authenticationProvider.getAuthentication().getUser());
		tradeMembershipEntity.setType(TradeMembershipEntity.Type.OWNER);
		tradeMembershipRepository.save(tradeMembershipEntity);
		// Transform the response
		TradeJson result = TradeTransformer.transform(tradeEntity);
		return result;
	}
	
	@RequestMapping(path="/{tradeId}", method=RequestMethod.PUT)
	public TradeJson put(@PathVariable Integer tradeId, @RequestBody TradeJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		requestJson.setTradeId(tradeId);
		tradeValidador.validatePut(requestJson, authenticationProvider.getAuthentication().getUser());
		// Transform the request
		TradeEntity tradeEntity = tradeTransformer.transform(requestJson, false);
		// Delegate to Repository layer
		tradeRepository.save(tradeEntity);
		// Make authenticated user the owner of the trade
		TradeMembershipEntity tradeMembershipEntity = new TradeMembershipEntity();
		tradeMembershipEntity.setTrade(tradeEntity);
		tradeMembershipEntity.setUser(authenticationProvider.getAuthentication().getUser());
		tradeMembershipEntity.setType(TradeMembershipEntity.Type.OWNER);
		tradeMembershipRepository.save(tradeMembershipEntity);
		// Transform the response
		TradeJson result = TradeTransformer.transform(tradeEntity);
		return result;
	}
	
	@RequestMapping(path="/{tradeId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer tradeId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		tradeValidador.validateDelete(tradeId);
		// Delegate to Repository layer
		tradeRepository.delete(tradeId);
	}

	@RequestMapping(path={"", "/"}, method=RequestMethod.GET)
	public SearchResult<TradeJson> get(String name, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		if (name != null) {
			searchCriteria.addCriterion(TradeEntity.Field.name, name);
		}
		// Delegate to Repository layer
		SearchResult<TradeEntity> searchResult = tradeRepository.search(searchCriteria);
		// Transform the response
		SearchResult<TradeJson> result = TradeTransformer.transform(searchResult);
		return result;
	}
	
	@RequestMapping(path="/{tradeId}", method=RequestMethod.GET)
	public TradeJson get(@PathVariable("tradeId") Integer tradeId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Delegate to Repository layer
		TradeEntity searchResult = tradeRepository.get(tradeId);
		// Transform the response
		TradeJson result = TradeTransformer.transform(searchResult);
		return result;
	}
}
