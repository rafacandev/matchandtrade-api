package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.JsonTestUtil;
import com.matchandtrade.test.helper.ArticleHelper;
import com.matchandtrade.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
@WebAppConfiguration
public class ArticleControllerIT extends BaseControllerIT {

	@Autowired
	private ArticleHelper articleHelper;
	private ArticleTransformer articleTransformer = new ArticleTransformer();

	@Before
	public void before() {
		super.before();
	}

	@Test
	public void delete_When_DeleteByArticleId_Then_Succeeds() throws Exception {
		ArticleEntity expected = articleHelper.createPersistedEntity(authenticatedUser);
		mockMvc.perform(
			delete("/matchandtrade-api/v1/articles/{articleId}", expected.getArticleId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isNoContent());
	}

	@Test
	public void get_When_GetByArticleId_Then_Succeeds() throws Exception {
		ArticleEntity expectedEntity = articleHelper.createPersistedEntity();
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
		articleHelper.createPersistedEntity();
		articleHelper.createPersistedEntity();
		articleHelper.createPersistedEntity();
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
		ArticleJson expected = ArticleHelper.createRandomJson();
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
		ArticleEntity expectedEntity = articleHelper.createPersistedEntity(authenticatedUser);
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
