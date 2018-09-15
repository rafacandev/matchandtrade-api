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
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.MembershipService;
import com.matchandtrade.rest.v1.json.MembershipJson;
import com.matchandtrade.rest.v1.link.MembershipLinkAssember;
import com.matchandtrade.rest.v1.transformer.MembershipTransformer;
import com.matchandtrade.rest.v1.validator.MembershipValidator;

@RestController
@RequestMapping(path="/matchandtrade-api/v1/memberships")
public class MembershipController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	MembershipValidator membershipValidador;
	@Autowired
	MembershipTransformer membershipTransformer;
	@Autowired
	MembershipService membershipService;
	
	@RequestMapping(path="/", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public MembershipJson post(@RequestBody MembershipJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		membershipValidador.validatePost(requestJson);
		// Transform the request
		MembershipEntity membershipEntity = membershipTransformer.transform(requestJson);
		// Delegate to service layer
		membershipService.create(membershipEntity);
		// Transform the response
		MembershipJson response = MembershipTransformer.transform(membershipEntity);
		// Assemble links
		MembershipLinkAssember.assemble(response);
		return response;
	}
	
	@RequestMapping(path="/{membershipId}", method=RequestMethod.GET)
	public MembershipJson get(@PathVariable("membershipId") Integer membershipId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request - Nothing to validate
		// Delegate to service layer
		MembershipEntity searchResult = membershipService.get(membershipId);
		// Transform the response
		MembershipJson response = MembershipTransformer.transform(searchResult);
		// Assemble links
		MembershipLinkAssember.assemble(response);		
		return response;
	}
	
	@RequestMapping(path={"", "/"}, method=RequestMethod.GET)
	public SearchResult<MembershipJson> get(Integer tradeId, Integer userId, MembershipEntity.Type type, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request - Nothing to validate
		membershipValidador.validateGet(_pageNumber, _pageSize);
		// Delegate to Service layer
		SearchResult<MembershipEntity> searchResult = membershipService.searchByTradeIdUserIdType(tradeId, userId, type, _pageNumber, _pageSize);
		// Transform the response
		SearchResult<MembershipJson> response = MembershipTransformer.transform(searchResult);
		// Assemble links
		MembershipLinkAssember.assemble(response);		
		return response;
	}
	
	@RequestMapping(path="/{membershipId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer membershipId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		membershipValidador.validateDelete(membershipId);
		// Delegate to Service layer
		membershipService.delete(membershipId);
	}
	
}
