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
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.link.ArticleLinkAssember;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.rest.v1.validator.ArticleValidator;

@RestController
@RequestMapping(path = "/matchandtrade-web-api/v1/articles")
public class ArticleController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private ArticleValidator articleValidator;

	@RequestMapping(path = "/{articleId}", method = RequestMethod.GET)
	public ArticleJson get(@PathVariable("articleId") Integer articleId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request - nothing to validate
		// Delegate to service layer
		ArticleEntity entity = articleService.get(articleId);
		// Transform the response
		ArticleJson response = ArticleTransformer.transform(entity);
		// Assemble links
		ArticleLinkAssember.assemble(response);
		return response;
	}
	
	@RequestMapping(path = "/", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public ArticleJson post(@RequestBody ArticleJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		articleValidator.validatePost(requestJson);
		// Transform the request
		ArticleEntity entity = ArticleTransformer.transform(requestJson);
		// Delegate to service layer
		articleService.create(entity);
		// Transform the response
		ArticleJson response = ArticleTransformer.transform(entity);
		// Assemble links
		ArticleLinkAssember.assemble(response);
		return response;
	}
	
	@RequestMapping(path = "/{articleId}", method = RequestMethod.PUT)
	public ArticleJson put(@PathVariable Integer articleId, @RequestBody ArticleJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		requestJson.setArticleId(articleId); // Always get the id from the URL when working on PUT methods
		articleValidator.validatePut(authenticationProvider.getAuthentication().getUser().getUserId(), articleId, requestJson);
		// Transform the request
		ArticleEntity entity = ArticleTransformer.transform(requestJson);
		// Delegate to service layer
		articleService.update(entity);
		// Transform the response
		ArticleJson response = ArticleTransformer.transform(entity);
		// Assemble links
		ArticleLinkAssember.assemble(response);
		return response;
	}

}