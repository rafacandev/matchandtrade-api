package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.test.DefaultTestingConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class AuthenticationControllerIT extends BaseControllerIT {

	@Before
	public void before() {
		super.before();
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
