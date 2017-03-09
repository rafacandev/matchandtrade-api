package com.matchandtrade.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authentication.UserAuthentication;
import com.matchandtrade.config.AuthenticationProperties;
import com.matchandtrade.model.AuthenticationModel;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;

@RestController
public class Controller {

	@Autowired
	private AuthenticationModel authenticationModel;
	@Autowired
	private UserModel userModel;
	@Autowired
	private HttpServletRequest httpRequest;
	
	public UserAuthentication getUserAuthentication() {
		UserAuthentication result = null;
		String authenticationHeader = httpRequest.getHeader(AuthenticationProperties.AUTHENTICATION_HEADER);
		AuthenticationEntity authenticationEntity = authenticationModel.getByToken(authenticationHeader);
		if (authenticationEntity != null) {
			UserEntity userEntity = userModel.get(authenticationEntity.getUserId());
			result = new UserAuthentication();
			result.setAuthenticated(true);
			result.setEmail(userEntity.getEmail());
			result.setName(userEntity.getName());
			result.setNewUser(false);
			result.setUserId(userEntity.getUserId());
		}
//    	HttpSession  session = httpRequest.getSession(false);
//    	if (session != null) {
//    		result = (UserAuthentication) session.getAttribute("user");
//		}
        return result;
	}
	
	public AuthenticationEntity getAuthentication() {
		String authenticationHeader = httpRequest.getHeader(AuthenticationProperties.AUTHENTICATION_HEADER);
		return authenticationModel.getByToken(authenticationHeader);
	}
	
	public void setHttpServletRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

}
