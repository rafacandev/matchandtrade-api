package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.rest.v1.transformer.AttachmentTransformer;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.AttachmentHelper;
import com.matchandtrade.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class AttachmentControllerIT extends BaseControllerIT {
	@Autowired
	private AttachmentHelper attachmentHelper;
	private AttachmentTransformer attachmentTransformer = new AttachmentTransformer();

	@Before
	public void before() {
		super.before();
	}

	@Test
	public void get_When_AttachmentExists_Then_Succeeds() throws Exception {
		AttachmentEntity givenAttachment = attachmentHelper.createPersistedEntity();
		MockHttpServletResponse response = mockMvc.perform(
			get("/matchandtrade-api/v1/attachments/{attachmentId}", givenAttachment.getAttachmentId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();
		AttachmentJson actual = JsonUtil.fromString(response.getContentAsString(), AttachmentJson.class);
		AttachmentJson expected = attachmentTransformer.transform(givenAttachment);
		assertEquals(expected, actual);
	}
//
//	@Test
//	public void get_When_GetAllByArticleId_Then_Succeeds() throws Exception {
//		ArticleEntity existingArticle = articleHelper.createPersistedEntity();
//		AttachmentEntity existingAttachment = attachmentHelper.createPersistedEntity(existingArticle);
//		String response = mockMvc.perform(
//			get("/matchandtrade-api/v1/articles/{articleId}/attachments", existingArticle.getArticleId())
//				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
//			)
//			.andExpect(status().isOk())
//			.andReturn()
//			.getResponse()
//			.getContentAsString();
//		List<AttachmentJson> actual = JsonUtil.fromArrayString(response, AttachmentJson.class);
//		assertEquals(1, actual.size());
//		AttachmentJson expectedAttachment = attachmentTransformer.transform(existingAttachment);
//		assertEquals(expectedAttachment, actual.get(0));
//	}
//
//
//	@Test
//	public void delete_When_AttachmentExists_Then_Succeeds() throws Exception {
//		ArticleEntity existingArticle = articleHelper.createPersistedEntity(authenticatedUser);
//		AttachmentEntity givenAttachment = attachmentHelper.createPersistedEntity(existingArticle);
//		mockMvc.perform(
//			delete("/matchandtrade-api/v1/articles/{articleId}/attachments/{attachmentId}", existingArticle.getArticleId(), givenAttachment.getAttachmentId())
//				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
//		)
//			.andExpect(status().isNoContent());
//	}
//
//	@Test
//	public void post_When_NewArticle_Then_Succeeds() throws Exception {
//		ArticleEntity existingArticle = articleHelper.createPersistedEntity(authenticatedUser);
//		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
//			.multipart("/matchandtrade-api/v1/articles/{articleId}/attachments/", existingArticle.getArticleId())
//			.file(multipartFile);
//		mockMvc.perform(request.header(HttpHeaders.AUTHORIZATION, authorizationHeader)).andExpect(status().isCreated());
//	}
}
