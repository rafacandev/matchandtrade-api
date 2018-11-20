package com.matchandtrade.test.random;

import java.io.IOException;
import java.io.InputStream;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.service.AttachmentService;

@Component
public class AttachmentRandom {

	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	
	public AttachmentEntity createPersistedEntity() {
		MultipartFile file = newSampleMockMultiPartFile();
		return attachmentService.create(file);
	}

	@Transactional
	public AttachmentEntity createPersistedEntity(ArticleEntity article) {
		MultipartFile file = newSampleMockMultiPartFile();
		AttachmentEntity attachment = attachmentService.create(file);
		// We need the persisted entity so JPA can save it instead of trying to create a new one
		ArticleEntity persistedArticle = articleRepositoryFacade.find(article.getArticleId());
		persistedArticle.getAttachments().add(attachment);
		articleRepositoryFacade.save(persistedArticle);
		// Also adding the attachment to the original article for consistency
		article.getAttachments().add(attachment);
		return attachment;
	}

	public static MockMultipartFile newSampleMockMultiPartFile() {
		String imageResource = "image-landscape.png";
		MockMultipartFile multipartFile;
		try {
			InputStream imageInputStream = AttachmentRandom.class.getClassLoader().getResource(imageResource).openStream();
			multipartFile = new MockMultipartFile("file", imageResource, "image/jpeg", imageInputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return multipartFile;
	}

}