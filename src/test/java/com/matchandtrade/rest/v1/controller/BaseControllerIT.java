package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.test.helper.ControllerHelper;
import com.matchandtrade.test.helper.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class BaseControllerIT {

	protected String authorizationHeader;
	@Autowired
	protected ControllerHelper controllerHelper;
	protected MockMvc mockMvc;
	protected UserEntity authenticatedUser;
	@Autowired
	protected UserHelper userHelper;
	@Autowired
	protected WebApplicationContext webApplicationContext;

	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		// Reusing authenticatedUser and authorization header for better performance
		if (authenticatedUser == null) {
			authenticatedUser = userHelper.createPersistedEntity();
			authorizationHeader = controllerHelper.generateAuthorizationHeader(authenticatedUser);
		}
	}

}
