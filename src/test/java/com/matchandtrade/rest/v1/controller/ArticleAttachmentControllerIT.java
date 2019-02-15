package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.JsonTestUtil;
import com.matchandtrade.test.helper.ArticleHelper;
import com.matchandtrade.test.helper.AttachmentHelper;
import com.matchandtrade.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class ArticleAttachmentControllerIT extends BaseControllerIT {
	@Autowired
	private ArticleHelper articleHelper;
	@Autowired
	private AttachmentHelper attachmentHelper;

	@Before
	public void before() {
		super.before();
	}

	@Test
	public void put_When_NewArticle_Then_StatusIsOk() throws Exception {
		ArticleEntity existingArticle = articleHelper.createPersistedEntity();
		AttachmentEntity existingAttachment = attachmentHelper.createPersistedEntity();
		mockMvc
			.perform(
				put("/matchandtrade-api/v1/articles/{articleId}/attachments/{attachmentId}",
					existingArticle.getArticleId(), existingAttachment.getAttachmentId())
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isOk());
	}

	@Test
	public void get_When_AttachmentExists_Then_StatusIsOk() throws Exception {
		ArticleEntity existingArticle = articleHelper.createPersistedEntity();
		AttachmentEntity existingAttachment = attachmentHelper.createPersistedEntity(existingArticle);
		String response = mockMvc
			.perform(
				get("/matchandtrade-api/v1/articles/{articleId}/attachments/", existingArticle.getArticleId())
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();
		List<AttachmentJson> attachments = JsonTestUtil.fromArrayString(response, AttachmentJson.class);
		AttachmentJson actual = attachments.get(0);
		assertEquals(existingAttachment.getAttachmentId(), actual.getAttachmentId());
	}
}
