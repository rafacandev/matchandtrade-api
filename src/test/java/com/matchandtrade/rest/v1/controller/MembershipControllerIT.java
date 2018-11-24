package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.MembershipJson;
import com.matchandtrade.rest.v1.transformer.MembershipTransformer;
import com.matchandtrade.test.helper.*;
import com.matchandtrade.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "file:config/matchandtrade.properties")
@SpringBootTest
@WebAppConfiguration
public class MembershipControllerIT {

	private String authorizationHeader;
	@Autowired
	private ControllerHelper controllerHelper;
	private MockMvc mockMvc;
	@Autowired
	private MembershipHelper membershipHelper;
	@Autowired
	private MembershipTransformer membershipTransformer;
	private UserEntity user;
	@Autowired
	private UserHelper userHelper;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private TradeHelper tradeHelper;

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
	public void delete_When_MembershipExists_Then_Succeeds() throws Exception {
		MembershipEntity expected = membershipHelper.createPersistedEntity(user);
		mockMvc
			.perform(
				delete("/matchandtrade-api/v1/memberships/{membershipId}", expected.getMembershipId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isNoContent());
	}

	@Test
	public void get_When_MembershipExists_Then_Succeeds() throws Exception {
		TradeEntity trade = tradeHelper.createPersistedEntity();
		MembershipEntity expectedEntity = membershipHelper.subscribeUserToTrade(user, trade);
		MembershipJson expected = membershipTransformer.transform(expectedEntity);

		MockHttpServletResponse response = mockMvc
			.perform(
				get("/matchandtrade-api/v1/memberships/{membershipId}", expected.getMembershipId())
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtil.toJson(expected))
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();

		MembershipJson actual = JsonUtil.fromString(response.getContentAsString(), MembershipJson.class);
		assertEquals(expected, actual);
	}

	@Test
	public void get_When_GetByUserId_Then_Succeeds() throws Exception {
		UserEntity owner = userHelper.createPersistedEntity();
		MembershipEntity firstMembership = membershipHelper.createPersistedEntity(owner);
		MembershipEntity secondMembership = membershipHelper.createPersistedEntity(owner);
		MockHttpServletResponse response = mockMvc
			.perform(
				get("/matchandtrade-api/v1/memberships?userId={userId}", owner.getUserId())
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();

		List<MembershipJson> actual = JsonUtil.fromArrayString(response.getContentAsString(), MembershipJson.class);
		assertTrue(actual.contains(membershipTransformer.transform(firstMembership)));
		assertTrue(actual.contains(membershipTransformer.transform(secondMembership)));
	}

	@Test
	public void post_When_MembershipDoesNotExist_Then_Succeeds() throws Exception {
		UserEntity existingUser = userHelper.createPersistedEntity();
		TradeEntity existingTrade = tradeHelper.createPersistedEntity(existingUser);
		MembershipJson expected = new MembershipJson();
		expected.setUserId(user.getUserId());
		expected.setTradeId(existingTrade.getTradeId());
		mockMvc
			.perform(
				post("/matchandtrade-api/v1/memberships/")
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtil.toJson(expected))
			)
			.andExpect(status().isCreated());
	}

}
