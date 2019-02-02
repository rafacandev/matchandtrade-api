package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.Entity;
import com.matchandtrade.rest.service.AuthenticationService;
import com.matchandtrade.rest.Json;
import com.matchandtrade.rest.service.SearchRecipeService;
import com.matchandtrade.rest.v1.json.search.Recipe;
import com.matchandtrade.rest.v1.json.search.SearchCriteriaJson;
import com.matchandtrade.rest.v1.linkassembler.SearchLinkAssembler;
import com.matchandtrade.rest.v1.transformer.SearchTransformer;
import com.matchandtrade.rest.v1.validator.SearchValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/matchandtrade-api/v1/search")
public class SearchController implements Controller {
	@Autowired
	AuthenticationService authenticationService;
	@Autowired
	private SearchLinkAssembler searchLinkAssembler;
	@Autowired
	private SearchRecipeService searchRecipeService;

	@RequestMapping(path={"", "/"}, method=RequestMethod.POST)
	public SearchResult<Json> post(@RequestBody SearchCriteriaJson request, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request
		SearchValidator.validatePost(request, _pageNumber, _pageSize);
		// Transform the request
		SearchCriteria searchCriteria = SearchTransformer.transform(request, _pageNumber, _pageSize);
		// Delegate to the service layer
		SearchResult<Entity> searchResult = searchRecipeService.search(searchCriteria);
		// Transform the response
		SearchResult<Json> response = SearchTransformer.transform(searchResult, Recipe.valueOf(request.getRecipe()));
		// Assemble links
		searchLinkAssembler.assemble(response);
		return response;
	}
}
