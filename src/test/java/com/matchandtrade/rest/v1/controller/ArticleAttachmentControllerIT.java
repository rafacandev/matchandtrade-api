package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.rest.v1.transformer.MembershipTransformer;
import com.matchandtrade.test.helper.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "file:config/matchandtrade.properties")
@SpringBootTest
@WebAppConfiguration
public class ArticleAttachmentControllerIT {

	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private AttachmentRandom attachmentRandom;
	private ArticleTransformer articleTransformer = new ArticleTransformer();
	private String authorizationHeader;
	@Autowired
	private ControllerHelper controllerHelper;
	private MockMvc mockMvc;
	@Autowired
	private MembershipRandom membershipRandom;
	@Autowired
	private MembershipTransformer membershipTransformer;
	private MockMultipartFile multipartFile;
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
		multipartFile = AttachmentRandom.newSampleMockMultiPartFile();
	}

	@Test
	public void post_When_NewArticle_Then_Succeeds() throws Exception {
		ArticleEntity expectedArticle = articleRandom.createPersistedEntity(user);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.fileUpload("/matchandtrade-api/v1/articles/{articleId}/attachments/", expectedArticle.getArticleId())
			.file(multipartFile);
		mockMvc.perform(request.header(HttpHeaders.AUTHORIZATION, authorizationHeader)).andExpect(status().isCreated());
	}

	@Test
	public void delete_When_AttachmentExists_Then_Succeeds() throws Exception {
		ArticleEntity expectedArticle = articleRandom.createPersistedEntity(user);
		AttachmentEntity expected = attachmentRandom.createPersistedEntity(expectedArticle);
		mockMvc.perform(
				delete("/matchandtrade-api/v1/articles/{articleId}/attachments/{attachmentId}", expectedArticle.getArticleId(), expected.getAttachmentId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isNoContent());
	}

}
