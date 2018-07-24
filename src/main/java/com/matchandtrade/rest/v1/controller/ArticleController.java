package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
//
//	@RequestMapping(path="/{tradeMembershipId}/items/{articleId}", method=RequestMethod.DELETE)
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public void delete(@PathVariable("tradeMembershipId") Integer tradeMembershipId, @PathVariable("articleId") Integer articleId) {
//		// Validate request identity
//		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
//		// Validate the request
//		itemValidator.validateDelete(tradeMembershipId, authenticationProvider.getAuthentication().getUser().getUserId(), articleId);
//		// Delegate to service layer
//		itemService.delete(tradeMembershipId, articleId);
//	}
//
	@RequestMapping(path = "/", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public ArticleJson post(@RequestBody ArticleJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		articleValidator.validatePost(authenticationProvider.getAuthentication().getUser().getUserId(), requestJson);
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
//
//	@RequestMapping(path = "/{tradeMembershipId}/items/{articleId}", method = RequestMethod.PUT)
//	public ItemJson put(@PathVariable Integer tradeMembershipId, @PathVariable Integer articleId, @RequestBody ItemJson requestJson) {
//		// Validate request identity
//		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
//		// Validate the request
//		requestJson.setArticleId(articleId); // Always get the id from the URL when working on PUT methods
//		itemValidator.validatePut(authenticationProvider.getAuthentication().getUser().getUserId(), tradeMembershipId, articleId, requestJson);
//		// Transform the request
//		ItemEntity itemEntity = ItemTransformer.transform(requestJson);
//		// Delegate to service layer
//		itemService.update(itemEntity);
//		// Transform the response
//		ItemJson response = ItemTransformer.transform(itemEntity);
//		// Assemble links
//		ItemLinkAssember.assemble(response, tradeMembershipId);
//		return response;
//	}
//
//	@RequestMapping(path="/{tradeMembershipId}/items/{articleId}", method=RequestMethod.GET)
//	public ItemJson get(@PathVariable("tradeMembershipId") Integer tradeMembershipId, @PathVariable("articleId") Integer articleId) {
//		// Validate request identity
//		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
//		// Validate the request
//		itemValidator.validateGet(authenticationProvider.getAuthentication().getUser().getUserId(), tradeMembershipId);
//		// Delegate to service layer
//		ItemEntity itemEntity = itemService.get(articleId);
//		// Transform the response
//		ItemJson response = ItemTransformer.transform(itemEntity);
//		// Assemble links
//		ItemLinkAssember.assemble(response, tradeMembershipId);
//		return response;
//	}
//
//	@RequestMapping(path={"/{tradeMembershipId}/items/", "/{tradeMembershipId}/items"}, method=RequestMethod.GET)
//	public SearchResult<ItemJson> get(@PathVariable("tradeMembershipId") Integer tradeMembershipId, Integer _pageNumber, Integer _pageSize) {
//		// Validate request identity
//		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
//		// Validate the request
//		itemValidator.validateGet(authenticationProvider.getAuthentication().getUser().getUserId(), tradeMembershipId, _pageNumber, _pageSize);
//		// Delegate to service layer
//		SearchResult<ItemEntity> searchResult = itemService.searchByTradeMembershipId(tradeMembershipId, _pageNumber, _pageSize);
//		// Transform the response
//		SearchResult<ItemJson> response = ItemTransformer.transform(searchResult);
//		// Assemble links
//		ItemLinkAssember.assemble(response, tradeMembershipId);
//		return response;
//	}
	
}
