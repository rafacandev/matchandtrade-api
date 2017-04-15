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
import com.matchandtrade.model.TradeModel;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;
import com.matchandtrade.rest.v1.validator.TradeValidator;

@RestController
@RequestMapping(path="/rest/v1/trades")
public class TradeController {

	@Autowired
	Authorization authorization;
	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	TradeModel tradeModel;
	@Autowired
	TradeValidator tradeValidador;
	@Autowired
	TradeTransformer tradeTransformer;
	
	@RequestMapping(path="/", method=RequestMethod.POST)
	public TradeJson post(@RequestBody TradeJson requestJson) {
		// Validate request identity
		authorization.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		tradeValidador.validatePost(requestJson);
		// Transform the request
		TradeEntity tradeEntity = tradeTransformer.transform(requestJson, false);
		// Delegate to model layer
		tradeModel.save(tradeEntity);
		// Transform the response
		TradeJson result = TradeTransformer.transform(tradeEntity);
		return result;
	}

	@RequestMapping(path={"", "/"}, method=RequestMethod.GET)
	public SearchResult<TradeJson> get(String name, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		authorization.validateIdentity(authenticationProvider.getAuthentication());
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		if (name != null) {
			searchCriteria.addCriterion(TradeEntity.Field.name, name);
		}
		// Delegate to model layer
		SearchResult<TradeEntity> searchResult = tradeModel.search(searchCriteria);
		// Transform the response
		SearchResult<TradeJson> result = TradeTransformer.transform(searchResult);
		return result;
	}
	
	@RequestMapping(path="/{tradeId}", method=RequestMethod.GET)
	public TradeJson get(@PathVariable("tradeId") Integer tradeId) {
		// Validate request identity
		authorization.validateIdentity(authenticationProvider.getAuthentication());
		// Delegate to model layer
		TradeEntity searchResult = tradeModel.get(tradeId);
		// Transform the response
		TradeJson result = TradeTransformer.transform(searchResult);
		return result;
	}
}
