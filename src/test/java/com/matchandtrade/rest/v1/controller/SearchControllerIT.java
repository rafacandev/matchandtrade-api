package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.json.search.Recipe;
import com.matchandtrade.rest.v1.json.search.SearchCriteriaJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.test.helper.ControllerHelper;
import com.matchandtrade.test.random.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "file:config/matchandtrade.properties")
@SpringBootTest
@WebAppConfiguration
public class SearchControllerIT {

	@Autowired
	private ArticleRandom articleRandom;
	private String authorizationHeader;
	@Autowired
	private ControllerHelper controllerHelper;
	private ArticleEntity expectedArticle;
	private MembershipEntity expectedMembership;
	private UserEntity expecteduser;
	private TradeEntity expectedTrade;
	@Autowired
	private ListingRandom listingRandom;
	private MockMvc mockMvc;
	@Autowired
	private MembershipRandom membershipRandom;
	@Autowired
	private UserRandom userRandom;
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Transactional
	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		// Reusing user and authorization header for better performance
		if (expecteduser == null) {
			expecteduser = userRandom.createPersistedEntity();
			authorizationHeader = controllerHelper.generateAuthorizationHeader(expecteduser);
			buildListingWhenUserOwnsArticleAndMembership();
		}
	}

	private void buildListingWhenUserOwnsArticleAndMembership() {
		expectedMembership = membershipRandom.createPersistedEntity(expecteduser);
		expectedArticle = articleRandom.createPersistedEntity(expectedMembership);
		expectedTrade = expectedMembership.getTrade();
		listingRandom.createPersisted(expectedArticle.getArticleId(), expectedMembership.getMembershipId());
	}

	@Test
	public void post_When_ArticleIsListedAndSearchingByArticleId_Then_Succeeds() throws Exception {
		SearchCriteriaJson request = new SearchCriteriaJson();
		request.setRecipe(Recipe.ARTICLES);
		request.addCriterion("Trade.tradeId", expectedTrade.getTradeId());
		String response = mockMvc
			.perform(
				post("/matchandtrade-api/v1/search/")
					.param("_pageNumber", "1")
					.param("_pageSize", "3")
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtil.toJson(request))
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();
		List<ArticleJson> actual = JsonUtil.fromArrayString(response, ArticleJson.class);
		ArticleTransformer articleTransformer = new ArticleTransformer();
		ArticleJson expected = articleTransformer.transform(expectedArticle);
		assertEquals(expected, actual.get(0));
	}

}
