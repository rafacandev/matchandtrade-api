package com.matchandtrade.rest.v1.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.Authorization;
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
	Authorization authorization;
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
		authorization.validateIdentity(authenticationProvider.getAuthentication());
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
}
