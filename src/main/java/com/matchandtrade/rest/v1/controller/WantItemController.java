package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.entity.WantItemEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.WantItemService;
import com.matchandtrade.rest.v1.json.WantItemJson;
import com.matchandtrade.rest.v1.link.WantItemLinkAssember;
import com.matchandtrade.rest.v1.transformer.WantItemTransformer;
import com.matchandtrade.rest.v1.validator.WantItemValidator;

@RestController
@RequestMapping(path = "/rest/v1/trade-memberships")
public class WantItemController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	private WantItemService wantItemService;
	@Autowired
	private WantItemValidator wantItemValidator;

	@RequestMapping(path = "/{tradeMembershipId}/items/{itemId}/want-items", method = RequestMethod.POST)
	public WantItemJson post(@PathVariable Integer tradeMembershipId, @PathVariable Integer itemId, @RequestBody WantItemJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		wantItemValidator.validatePost(tradeMembershipId, itemId, requestJson);
		// Transform the request
		WantItemEntity entity = WantItemTransformer.transform(requestJson);
		// Delegate to service layer
		wantItemService.create(entity, itemId);
		// Transform the response
		WantItemJson response = WantItemTransformer.transform(entity);
		// Assemble links
		WantItemLinkAssember.assemble(response, itemId, tradeMembershipId);
		return response;
	}

}
