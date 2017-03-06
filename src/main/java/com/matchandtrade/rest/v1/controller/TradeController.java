package com.matchandtrade.rest.v1.controller;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.Authorization;
import com.matchandtrade.model.TradeModel;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.rest.Controller;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;
import com.matchandtrade.rest.v1.validator.TradeValidator;

@RestController
@RequestMapping(path="/rest/v1/trades")
public class TradeController extends Controller {

	@Autowired
	private Authorization authorization;
	@Autowired
	private TradeModel model;
	@Autowired
	private TradeValidator tradeValidador;
	@Autowired
	private TradeTransformer tradeTransformer;

	@Transactional
	@RequestMapping(path="/", method=RequestMethod.POST)
	public TradeJson post(@RequestBody TradeJson requestJson) {
		// Check authorization for this operation
		authorization.doBasicAuthorization(getUserAuthentication());
		// Validate the request
		tradeValidador.validatePost(requestJson);
		// Transform the request
		
		TradeEntity tradeEntity = tradeTransformer.transform(requestJson, false);
		// Delegate to model layer
		model.save(tradeEntity);
		// Transform the response
		TradeJson result = TradeTransformer.transform(tradeEntity);
		return result;
	}
}
