package com.matchandtrade.rest.v1.controller;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.TradeResultService;
import com.matchandtrade.rest.v1.validator.TradeResultValidator;

@RestController
@RequestMapping(path="/matchandtrade-web-api/v1/trades")
public class TradeResultController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	TradeResultService tradeResultService;
	@Autowired
	TradeResultValidator tradeResultValidator;

	@RequestMapping(path="/{tradeId}/results",
			method = RequestMethod.GET,
			produces = "text/csv")
	public String get(@PathVariable("tradeId") Integer tradeId) throws IOException {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		tradeResultValidator.validateGet(tradeId);
		// Delegate to Service layer
		String response = tradeResultService.get(tradeId);
		// Transform the response - Nothing to transform
		// Assemble links - Nothing to assemble
		return response;
	}

}
