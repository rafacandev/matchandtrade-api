package com.matchandtrade.rest.v1.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.TradeResultService;
import com.matchandtrade.rest.v1.json.TradeResultJson;
import com.trademaximazer.TradeMaximizerTransformer;

@RestController
@RequestMapping(path="/rest/v1/trades")
public class TradeResultController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	TradeResultService tradeResultService;

	@RequestMapping(path="/{tradeId}/results", method=RequestMethod.GET)
	public List<TradeResultJson> getResults(@PathVariable("tradeId") Integer tradeId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request - Nothing to validate
		// Delegate to Service layer
		String result = tradeResultService.get(tradeId);
		// Transform the response
		List<TradeResultJson> response = TradeMaximizerTransformer.transform(result);
		// Assemble links
		// TODO Assemble links
		return response;
	}

}
