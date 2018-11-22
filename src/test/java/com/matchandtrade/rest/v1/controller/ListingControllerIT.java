package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.ListingJson;
import com.matchandtrade.rest.v1.transformer.MembershipTransformer;
import com.matchandtrade.test.helper.ControllerHelper;
import com.matchandtrade.test.helper.ArticleRandom;
import com.matchandtrade.test.helper.MembershipRandom;
import com.matchandtrade.test.helper.TradeRandom;
import com.matchandtrade.test.helper.UserRandom;
import com.matchandtrade.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
public class ListingControllerIT {

	@Autowired
	private ArticleRandom articleRandom;
	private String authorizationHeader;
	@Autowired
	private ControllerHelper controllerHelper;
	private MockMvc mockMvc;
	@Autowired
	private MembershipRandom membershipRandom;
	@Autowired
	private MembershipTransformer membershipTransformer;
	private UserEntity user;
	@Autowired
	private UserRandom userRandom;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private TradeRandom tradeRandom;

	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		// Reusing user and authorization header for better performance
		if (user == null) {
			user = userRandom.createPersistedEntity();
			authorizationHeader = controllerHelper.generateAuthorizationHeader(user);
		}
	}

	private ListingJson buildListingWhenUserOwnsArticleAndMembership() {
		ArticleEntity article = articleRandom.createPersistedEntity(user);
		MembershipEntity membership = membershipRandom.createPersistedEntity(user);
		ListingJson expected = new ListingJson();
		expected.setArticleId(article.getArticleId());
		expected.setMembershipId(membership.getMembershipId());
		return expected;
	}

	@Test
	public void post_When_UserOwnsArticleAndMembership_Then_Succeeds() throws Exception {
		ListingJson expected = buildListingWhenUserOwnsArticleAndMembership();
		mockMvc
			.perform(
				post("/matchandtrade-api/v1/listing/")
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtil.toJson(expected))
			)
			.andExpect(status().isCreated());
	}

	@Test
	public void delete_When_UserOwnsArticleAndMembership_Then_Succeeds() throws Exception {
		ListingJson expected = buildListingWhenUserOwnsArticleAndMembership();
		mockMvc
			.perform(
				delete("/matchandtrade-api/v1/listing/")
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtil.toJson(expected))
			)
			.andExpect(status().isNoContent());
	}

}
