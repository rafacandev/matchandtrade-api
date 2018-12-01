package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.MembershipJson;
import com.matchandtrade.rest.v1.transformer.MembershipTransformer;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.MembershipHelper;
import com.matchandtrade.test.helper.TradeHelper;
import com.matchandtrade.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class MembershipControllerIT extends BaseControllerIT{

	@Autowired
	private MembershipHelper membershipHelper;
	@Autowired
	private MembershipTransformer membershipTransformer;
	@Autowired
	private TradeHelper tradeHelper;

	@Before
	public void before() {
		super.before();
	}

	@Test
	public void delete_When_MembershipExists_Then_Succeeds() throws Exception {
		MembershipEntity expected = membershipHelper.createPersistedEntity(authenticatedUser);
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
		MembershipEntity expectedEntity = membershipHelper.subscribeUserToTrade(authenticatedUser, trade);
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
		expected.setUserId(authenticatedUser.getUserId());
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
