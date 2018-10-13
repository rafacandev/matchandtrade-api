package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.rest.service.MembershipArticleService;
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
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.link.ArticleLinkAssember;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.rest.v1.validator.MembershipArticleValidator;

@RestController
@RequestMapping(path = "/matchandtrade-api/v1/memberships")
public class MembershipArticleController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	private MembershipArticleService membershipArticleService;
	@Autowired
	private MembershipArticleValidator membershipArticleValidator;

	@RequestMapping(path="/{membershipId}/articles/{articleId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("membershipId") Integer membershipId, @PathVariable("articleId") Integer articleId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		membershipArticleValidator.validateDelete(membershipId, authenticationProvider.getAuthentication().getUser().getUserId(), articleId);
		// Delegate to service layer
		membershipArticleService.delete(membershipId, articleId);
	}

	@RequestMapping(path = "/{membershipId}/articles", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public ArticleJson post(@PathVariable Integer membershipId, @RequestBody ArticleJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		membershipArticleValidator.validatePost(authenticationProvider.getAuthentication().getUser().getUserId(), membershipId, requestJson);
		// Transform the request
		ArticleEntity articleEntity = ArticleTransformer.transform(requestJson);
		// Delegate to service layer
		membershipArticleService.create(membershipId, articleEntity);
		// Transform the response
		ArticleJson response = ArticleTransformer.transform(articleEntity);
		// Assemble links
		ArticleLinkAssember.assemble(response, membershipId);
		return response;
	}

	@RequestMapping(path = "/{membershipId}/articles/{articleId}", method = RequestMethod.PUT)
	public ArticleJson put(@PathVariable Integer membershipId, @PathVariable Integer articleId, @RequestBody ArticleJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		requestJson.setArticleId(articleId); // Always get the id from the URL when working on PUT methods
		membershipArticleValidator.validatePut(authenticationProvider.getAuthentication().getUser().getUserId(), membershipId, articleId, requestJson);
		// Transform the request
		ArticleEntity articleEntity = ArticleTransformer.transform(requestJson);
		// Delegate to service layer
		membershipArticleService.update(articleEntity);
		// Transform the response
		ArticleJson response = ArticleTransformer.transform(articleEntity);
		// Assemble links
		ArticleLinkAssember.assemble(response, membershipId);
		return response;
	}

	@RequestMapping(path="/{membershipId}/articles/{articleId}", method=RequestMethod.GET)
	public ArticleJson get(@PathVariable("membershipId") Integer membershipId, @PathVariable("articleId") Integer articleId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		membershipArticleValidator.validateGet(authenticationProvider.getAuthentication().getUser().getUserId(), membershipId);
		// Delegate to service layer
		ArticleEntity articleEntity = membershipArticleService.get(articleId);
		// Transform the response
		ArticleJson response = ArticleTransformer.transform(articleEntity);
		// Assemble links
		ArticleLinkAssember.assemble(response, membershipId);
		return response;
	}

	@RequestMapping(path={"/{membershipId}/articles/", "/{membershipId}/articles"}, method=RequestMethod.GET)
	public SearchResult<ArticleJson> get(@PathVariable("membershipId") Integer membershipId, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		membershipArticleValidator.validateGet(authenticationProvider.getAuthentication().getUser().getUserId(), membershipId, _pageNumber, _pageSize);
		// Delegate to service layer
		SearchResult<ArticleEntity> searchResult = membershipArticleService.searchByMembershipId(membershipId, _pageNumber, _pageSize);
		// Transform the response
		SearchResult<ArticleJson> response = ArticleTransformer.transform(searchResult);
		// Assemble links
		ArticleLinkAssember.assemble(response, membershipId);
		return response;
	}
	
}
