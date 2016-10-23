package com.matchandtrade.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authentication.UserAuthentication;

@RestController
public class Controller {
	
	@Autowired
	private HttpServletRequest httpRequest;
	
	public UserAuthentication getUserAuthentication() {
		UserAuthentication result = null;
    	HttpSession  session = httpRequest.getSession(false);
    	if (session != null) {
    		UserAuthentication user = (UserAuthentication) session.getAttribute("user");
    		if (user != null) {
    			result = transform(user);
    		}
		}
        return result;
	}
	
	public void setHttpServletRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}
	
	private UserAuthentication transform(UserAuthentication userAuthentication) {
		UserAuthentication result = new UserAuthentication();
		result.setAuthenticated(userAuthentication.isAuthenticated());
		result.setEmail(userAuthentication.getEmail());
		result.setName(userAuthentication.getName());
		result.setNewUser(userAuthentication.isNewUser());
		result.setUserId(userAuthentication.getUserId());
		return result;
	}

}
