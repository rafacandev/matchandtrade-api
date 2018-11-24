package com.matchandtrade.rest.v1.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

	@PostMapping("/")
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
		MembershipJson response = membershipTransformer.transform(membershipEntity);
		// Assemble links
		MembershipLinkAssember.assemble(response);
		return response;
	}

	@GetMapping("/{membershipId}")
	public MembershipJson get(@PathVariable("membershipId") Integer membershipId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request - Nothing to validate
		membershipValidador.validateGet(membershipId);
		// Delegate to service layer
		MembershipEntity searchResult = membershipService.find(membershipId);
		// Transform the response
		MembershipJson response = membershipTransformer.transform(searchResult);
		// Assemble links
		MembershipLinkAssember.assemble(response);		
		return response;
	}

	@GetMapping()
	public SearchResult<MembershipJson> get(Integer tradeId, Integer userId, MembershipEntity.Type type, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		membershipValidador.validateGet(_pageNumber, _pageSize);
		// Delegate to Service layer
		SearchResult<MembershipEntity> searchResult = membershipService.findByTradeIdUserIdType(tradeId, userId, type, _pageNumber, _pageSize);
		// Transform the response
		SearchResult<MembershipJson> response = membershipTransformer.transform(searchResult);
		// Assemble links
		MembershipLinkAssember.assemble(response);		
		return response;
	}

	@DeleteMapping("/{membershipId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer membershipId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		membershipValidador.validateDelete(authenticationProvider.getAuthentication().getUser(), membershipId);
		// Delegate to Service layer
		membershipService.delete(membershipId);
	}
	
}
