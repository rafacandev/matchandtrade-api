package com.matchandtrade.rest.v1.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.dto.ArticleAndMembershipIdDto;
import com.matchandtrade.persistence.entity.Entity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.Json;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.SearchWithRecipeService;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.json.search.Recipe;
import com.matchandtrade.rest.v1.json.search.SearchCriteriaJson;
import com.matchandtrade.rest.v1.link.ArticleLinkAssember;
import com.matchandtrade.rest.v1.transformer.SearchTransformer;
import com.matchandtrade.rest.v1.validator.SearchValidator;

@RestController
@RequestMapping(path="/matchandtrade-api/v1/search")
public class SearchController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	private SearchWithRecipeService searchService;
	
	@RequestMapping(path={"", "/"}, method=RequestMethod.POST)
	public SearchResult<Json> post(@RequestBody SearchCriteriaJson request, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		SearchValidator.validatePost(request, _pageNumber, _pageSize);
		// Transform the request
		SearchCriteria searchCriteria = SearchTransformer.transform(request, new Pagination(_pageNumber, _pageSize));
		// Delegate to service layer
		SearchResult<Entity> searchResult = search(request.getRecipe(), searchCriteria);
		// Transform the response
		SearchResult<Json> response = SearchTransformer.transform(searchResult, request.getRecipe());
		// Assemble links
		assembleLinks(response, request.getRecipe(), searchResult);
		return response;
	}

	private SearchResult<Entity> search(Recipe recipe, SearchCriteria searchCriteria) {
		if (Recipe.ARTICLES != recipe) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid SearchCriteriaJson.recipe: " + recipe);
		} else {
			return searchService.articleRecipe(searchCriteria);
		}
	}

	private void assembleLinks(SearchResult<Json> response, Recipe recipe, SearchResult<Entity> searchResult) {
		if (Recipe.ARTICLES == recipe) {
			// Sub-optimum performance but nice separation of concerns. If performance becomes issue then we need to assemble links along with with json transformation.
			searchResult.getResultList().forEach(v -> {
				ArticleAndMembershipIdDto articleAndMembershipIdDto = (ArticleAndMembershipIdDto) v;
				response.getResultList().forEach(json -> {
					ArticleJson articleJson = (ArticleJson) json;
					if (articleJson.getArticleId() == articleAndMembershipIdDto.getArticle().getArticleId()) {
						ArticleLinkAssember.assemble(articleJson, articleAndMembershipIdDto.getMembershipId());
					}
				});
			});
		}
	}

}
