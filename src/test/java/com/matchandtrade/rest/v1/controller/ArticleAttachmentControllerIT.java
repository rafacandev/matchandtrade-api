package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.ArticleHelper;
import com.matchandtrade.test.helper.AttachmentHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
@WebAppConfiguration
public class ArticleAttachmentControllerIT extends BaseControllerIT {

	@Autowired
	private ArticleHelper articleHelper;
	@Autowired
	private AttachmentHelper attachmentHelper;
	private MockMultipartFile multipartFile;

	@Before
	public void before() {
		super.before();
		multipartFile = AttachmentHelper.newSampleMockMultiPartFile();
	}

	@Test
	public void post_When_NewArticle_Then_Succeeds() throws Exception {
		ArticleEntity expectedArticle = articleHelper.createPersistedEntity(authenticatedUser);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.fileUpload("/matchandtrade-api/v1/articles/{articleId}/attachments/", expectedArticle.getArticleId())
			.file(multipartFile);
		mockMvc.perform(request.header(HttpHeaders.AUTHORIZATION, authorizationHeader)).andExpect(status().isCreated());
	}

	@Test
	public void delete_When_AttachmentExists_Then_Succeeds() throws Exception {
		ArticleEntity expectedArticle = articleHelper.createPersistedEntity(authenticatedUser);
		AttachmentEntity expected = attachmentHelper.createPersistedEntity(expectedArticle);
		mockMvc.perform(
				delete("/matchandtrade-api/v1/articles/{articleId}/attachments/{attachmentId}", expectedArticle.getArticleId(), expected.getAttachmentId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isNoContent());
	}

}
