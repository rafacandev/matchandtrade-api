package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;
import com.matchandtrade.test.helper.ControllerHelper;
import com.matchandtrade.test.helper.TradeHelper;
import com.matchandtrade.test.helper.UserHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "file:config/matchandtrade.properties")
@SpringBootTest
@WebAppConfiguration
public class AuthenticationControllerIT {

	private String authorizationHeader;
	@Autowired
	private ControllerHelper controllerHelper;
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	@Autowired
	private TradeHelper tradeHelper;
	@Autowired
	private TradeRepositoryFacade tradeRepositoryFacade;
	private TradeTransformer tradeTransformer = new TradeTransformer();
	private UserEntity user;
	@Autowired
	private UserHelper userHelper;


	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		// Reusing user and authorization header for better performance
		if (user == null) {
			user = userHelper.createPersistedEntity();
			authorizationHeader = controllerHelper.generateAuthorizationHeader(user);
		}
	}

	@Test
	public void get_When_AuthenticationExist_Then_Succeeds() throws Exception {
		mockMvc.perform(
				get("/matchandtrade-api/v1/authentications/")
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isOk());
	}

}
