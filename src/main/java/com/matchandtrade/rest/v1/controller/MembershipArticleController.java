package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.MembershipArticleService;
import com.matchandtrade.rest.v1.validator.MembershipArticleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/matchandtrade-api/v1/memberships")
public class MembershipArticleController {
	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	private MembershipArticleValidator membershipArticleValidator;
	@Autowired
	private MembershipArticleService membershipArticleService;

	@RequestMapping(path="/{membershipId}/articles/{articleId}", method= RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void post(Integer membershipId, Integer articleId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		membershipArticleValidator.validatePost(authenticationProvider.getAuthentication().getUser().getUserId(), membershipId, articleId);
		// Delegate to service layer
		membershipArticleService.create(membershipId, articleId);
		// TODO add heteroas
	}

	@RequestMapping(path="/{membershipId}/articles/{articleId}", method= RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.CREATED)
	public void delete(Integer membershipId, Integer articleId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		membershipArticleValidator.validateDelete(authenticationProvider.getAuthentication().getUser().getUserId(), membershipId, articleId);
		// Delegate to service layer
		membershipArticleService.delete(membershipId, articleId);
		// TODO add heteroas
	}
}
