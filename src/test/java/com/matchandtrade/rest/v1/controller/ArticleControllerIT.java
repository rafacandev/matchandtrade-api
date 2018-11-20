package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.json.ListingJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.rest.v1.transformer.MembershipTransformer;
import com.matchandtrade.test.JsonTestUtil;
import com.matchandtrade.test.helper.ControllerHelper;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.MembershipRandom;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "file:config/matchandtrade.properties")
@SpringBootTest
@WebAppConfiguration
public class ArticleControllerIT {

	@Autowired
	private ArticleRandom articleRandom;
	private ArticleTransformer articleTransformer = new ArticleTransformer();
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

	@Test
	public void delete_When_DeleteByArticleId_Then_Succeeds() throws Exception {
		ArticleEntity expectedEntity = articleRandom.createPersistedEntity();
		ArticleJson expected = articleTransformer.transform(expectedEntity);
		mockMvc.perform(
			delete("/matchandtrade-api/v1/articles/{articleId}", expected.getArticleId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isNoContent());
	}

	@Test
	public void get_When_GetByArticleId_Then_Succeeds() throws Exception {
		ArticleEntity expectedEntity = articleRandom.createPersistedEntity();
		ArticleJson expected = articleTransformer.transform(expectedEntity);
		String response = mockMvc.perform(
			get("/matchandtrade-api/v1/articles/{articleId}", expected.getArticleId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();
		ArticleJson actual = JsonUtil.fromString(response, ArticleJson.class);
		assertEquals(expected, actual);
	}

	@Test
	public void get_When_GetAllAndPageSizeIs2_Then_Returns2Articles() throws Exception {
		articleRandom.createPersistedEntity();
		articleRandom.createPersistedEntity();
		articleRandom.createPersistedEntity();
		MockHttpServletResponse response = mockMvc.perform(
			get("/matchandtrade-api/v1/articles?_pageNumber=1&_pageSize=2")
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();
		SearchResult<ArticleJson> actual = JsonTestUtil.fromSearchResultString(response, ArticleJson.class);
		assertEquals(2, actual.getResultList().size());
		assertEquals(2, actual.getPagination().getSize());
		assertEquals(1, actual.getPagination().getNumber());
		assertTrue(actual.getPagination().getTotal() > 3);
	}

	@Test
	public void post_When_NewArticle_Then_Succeeds() throws Exception {
		ArticleJson expected = ArticleRandom.createJson();
		mockMvc
			.perform(
				post("/matchandtrade-api/v1/articles/")
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtil.toJson(expected))
			)
			.andExpect(status().isCreated());
	}

	@Test
	public void put_When_ExistingArticle_Then_Succeeds() throws Exception {
		ArticleEntity expectedEntity = articleRandom.createPersistedEntity(user);
		expectedEntity.setName(expectedEntity.getName() + " - updated");
		ArticleJson expected = articleTransformer.transform(expectedEntity);
		String response = mockMvc
			.perform(
				put("/matchandtrade-api/v1/articles/{articleId}", expected.getArticleId())
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtil.toJson(expectedEntity))
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();
		ArticleJson actual = JsonUtil.fromString(response, ArticleJson.class);
		assertEquals(expected, actual);
	}

}
