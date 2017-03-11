package com.matchandtrade.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.config.AuthenticationProperties;
import com.matchandtrade.model.AuthenticationModel;
import com.matchandtrade.persistence.entity.AuthenticationEntity;

@RestController
public class Controller {

	@Autowired
	protected AuthenticationModel authenticationModel;
	@Autowired
	private HttpServletRequest httpRequest;
	
	public AuthenticationEntity getAuthentication() {
		String authenticationHeader = httpRequest.getHeader(AuthenticationProperties.AUTHENTICATION_HEADER);
		return authenticationModel.getByToken(authenticationHeader);
	}
	
	public void setHttpServletRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

}
