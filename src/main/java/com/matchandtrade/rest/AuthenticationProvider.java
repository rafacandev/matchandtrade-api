package com.matchandtrade.rest;

import javax.servlet.http.HttpServletRequest;

import com.matchandtrade.authentication.AuthenticationOAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.facade.AuthenticationRespositoryFacade;

@Component
@RequestScope
public class AuthenticationProvider {

	@Autowired
	protected AuthenticationRespositoryFacade authenticationRepository;
	@Autowired
	private HttpServletRequest httpRequest;
	
	public AuthenticationEntity getAuthentication() {
		String authenticationHeader = httpRequest.getHeader(AuthenticationOAuth.AUTHORIZATION_HEADER);
		return authenticationRepository.findByToken(authenticationHeader);
	}

}
