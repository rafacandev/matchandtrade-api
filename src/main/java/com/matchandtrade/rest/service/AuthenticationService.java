package com.matchandtrade.rest.service;

import com.matchandtrade.authentication.AuthenticationOAuth;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.facade.AuthenticationRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationService {
	@Autowired
	protected AuthenticationRepositoryFacade authenticationRepository;
	@Autowired
	private HttpServletRequest httpRequest;
	
	public AuthenticationEntity findCurrentAuthentication() {
		String authenticationHeader = httpRequest.getHeader(AuthenticationOAuth.AUTHORIZATION_HEADER);
		return authenticationRepository.findByToken(authenticationHeader);
	}
}
