package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.ArticleJson;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "file:config/matchandtrade.properties")
@SpringBootTest
@WebAppConfiguration
public class ArticleAttachmentControllerIT {

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
	public void post_When_NewArticle_Then_Succeeds() throws Exception {
		ArticleEntity expectedArticle = articleRandom.createPersistedEntity(user);
		MockMvcRequestBuilders.fileUpload("").file(new MockMultipartFile()).;

		mockMvc
			.perform(
				post("/matchandtrade-api/v1/articles/")
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isCreated());
	}

}
