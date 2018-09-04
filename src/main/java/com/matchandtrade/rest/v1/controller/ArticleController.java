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
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.link.ArticleLinkAssember;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.rest.v1.validator.ArticleValidator;

@RestController
@RequestMapping(path = "/matchandtrade-web-api/v1/trade-memberships")
public class ArticleController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private ArticleValidator articleValidator;

	@RequestMapping(path="/{tradeMembershipId}/articles/{articleId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("tradeMembershipId") Integer tradeMembershipId, @PathVariable("articleId") Integer articleId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		articleValidator.validateDelete(tradeMembershipId, authenticationProvider.getAuthentication().getUser().getUserId(), articleId);
		// Delegate to service layer
		articleService.delete(tradeMembershipId, articleId);
	}

	@RequestMapping(path = "/{tradeMembershipId}/articles", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public ArticleJson post(@PathVariable Integer tradeMembershipId, @RequestBody ArticleJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		articleValidator.validatePost(authenticationProvider.getAuthentication().getUser().getUserId(), tradeMembershipId, requestJson);
		// Transform the request
		ArticleEntity articleEntity = ArticleTransformer.transform(requestJson);
		// Delegate to service layer
		articleService.create(tradeMembershipId, articleEntity);
		// Transform the response
		ArticleJson response = ArticleTransformer.transform(articleEntity);
		// Assemble links
		ArticleLinkAssember.assemble(response, tradeMembershipId);
		return response;
	}

	@RequestMapping(path = "/{tradeMembershipId}/articles/{articleId}", method = RequestMethod.PUT)
	public ArticleJson put(@PathVariable Integer tradeMembershipId, @PathVariable Integer articleId, @RequestBody ArticleJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		requestJson.setArticleId(articleId); // Always get the id from the URL when working on PUT methods
		articleValidator.validatePut(authenticationProvider.getAuthentication().getUser().getUserId(), tradeMembershipId, articleId, requestJson);
		// Transform the request
		ArticleEntity articleEntity = ArticleTransformer.transform(requestJson);
		// Delegate to service layer
		articleService.update(articleEntity);
		// Transform the response
		ArticleJson response = ArticleTransformer.transform(articleEntity);
		// Assemble links
		ArticleLinkAssember.assemble(response, tradeMembershipId);
		return response;
	}

	@RequestMapping(path="/{tradeMembershipId}/articles/{articleId}", method=RequestMethod.GET)
	public ArticleJson get(@PathVariable("tradeMembershipId") Integer tradeMembershipId, @PathVariable("articleId") Integer articleId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		articleValidator.validateGet(authenticationProvider.getAuthentication().getUser().getUserId(), tradeMembershipId);
		// Delegate to service layer
		ArticleEntity articleEntity = articleService.get(articleId);
		// Transform the response
		ArticleJson response = ArticleTransformer.transform(articleEntity);
		// Assemble links
		ArticleLinkAssember.assemble(response, tradeMembershipId);
		return response;
	}

	@RequestMapping(path={"/{tradeMembershipId}/articles/", "/{tradeMembershipId}/articles"}, method=RequestMethod.GET)
	public SearchResult<ArticleJson> get(@PathVariable("tradeMembershipId") Integer tradeMembershipId, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		articleValidator.validateGet(authenticationProvider.getAuthentication().getUser().getUserId(), tradeMembershipId, _pageNumber, _pageSize);
		// Delegate to service layer
		SearchResult<ArticleEntity> searchResult = articleService.searchByTradeMembershipId(tradeMembershipId, _pageNumber, _pageSize);
		// Transform the response
		SearchResult<ArticleJson> response = ArticleTransformer.transform(searchResult);
		// Assemble links
		ArticleLinkAssember.assemble(response, tradeMembershipId);
		return response;
	}
	
}
