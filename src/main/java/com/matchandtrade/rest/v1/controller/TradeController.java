package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.rest.service.AuthenticationService;
import com.matchandtrade.rest.service.TradeService;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.rest.v1.linkassembler.TradeLinkAssembler;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;
import com.matchandtrade.rest.v1.validator.TradeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@ExposesResourceFor(TradeJson.class)
@RequestMapping(path="/matchandtrade-api/v1/trades")
public class TradeController implements Controller {

	@Autowired
	AuthenticationService authenticationService;
	@Autowired
	TradeValidator tradeValidador;
	@Autowired
	TradeService tradeService;
	private TradeTransformer tradeTransformer = new TradeTransformer();
	@Autowired
	private TradeLinkAssembler tradeLinkAssembler;

	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public TradeJson post(@RequestBody TradeJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request
		tradeValidador.validatePost(requestJson);
		// Transform the request
		TradeEntity tradeEntity = tradeTransformer.transform(requestJson);
		// Delegate to service layer
		tradeService.create(tradeEntity, authenticationService.findCurrentAuthentication().getUser());
		// Transform the response
		TradeJson response = tradeTransformer.transform(tradeEntity);
		// TODO: Assemble links
		return response;
	}

	@PutMapping("/{id}")
	public TradeJson put(@PathVariable("id") Integer tradeId, @RequestBody TradeJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request
		requestJson.setTradeId(tradeId); // Always get the id from the URL when working on PUT methods
		tradeValidador.validatePut(requestJson, authenticationService.findCurrentAuthentication().getUser());
		// Transform the request
		TradeEntity tradeEntity = tradeTransformer.transform(requestJson);
		// Delegate to service layer
		tradeService.update(tradeEntity);
		// Transform the response
		TradeJson response = tradeTransformer.transform(tradeEntity);
		// TODO: Assemble links
		return response;
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Integer tradeId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request
		tradeValidador.validateDelete(authenticationService.findCurrentAuthentication().getUser(), tradeId);
		// Delegate to service layer
		tradeService.delete(tradeId);
	}

	@GetMapping(path = {"", "/"})
	public SearchResult<TradeJson> get(Integer _pageNumber, Integer _pageSize) {
		// Validate request identity - Nothing to validate it is a public resource
		// Validate the request - Nothing to validate
		tradeValidador.validateGet(_pageNumber, _pageSize);
		// Delegate to Service layer
		SearchResult<TradeEntity> searchResult = tradeService.findAll(_pageNumber, _pageSize);
		// Transform the response
		SearchResult<TradeJson> response = tradeTransformer.transform(searchResult);
		tradeLinkAssembler.assemble(response);
		return response;
	}

	@GetMapping("/{id}")
	public TradeJson get(@PathVariable("id") Integer tradeId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request
		tradeValidador.validateGet(tradeId);
		// Delegate to Service layer
		TradeEntity tradeEntity = tradeService.findByTradeId(tradeId);
		// Transform the response
		TradeJson response = tradeTransformer.transform(tradeEntity);
		// TODO: Assemble links
		return response;
	}

}
