package com.matchandtrade.test.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.service.AttachmentService;

@Component
@Transactional
@Commit
public class AttachmentHelper {

	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	
	@Transactional
	public AttachmentEntity createPersistedEntity(ArticleEntity article) {
		MultipartFile file = newSampleMockMultiPartFile();
		AttachmentEntity result = attachmentService.create(file);
		ArticleEntity persistedArticle = articleRepositoryFacade.find(article.getArticleId());
		persistedArticle.getAttachments().add(result);
		articleRepositoryFacade.save(persistedArticle);
		// Also adding the attachment to the original article for consistency
		article.getAttachments().add(result);
		return result;
	}

	public static MockMultipartFile newSampleMockMultiPartFile() {
		String imageResource = "image-landscape.png";
		MockMultipartFile result;
		try {
			InputStream imageInputStream = AttachmentHelper.class.getClassLoader().getResource(imageResource).openStream();
			result = new MockMultipartFile("file", imageResource, "image/jpeg", imageInputStream);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return result;
	}

}