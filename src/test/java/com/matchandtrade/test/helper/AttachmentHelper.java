package com.matchandtrade.test.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

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
		MockMultipartFile file = newMockMultiPartFileImage();
		AttachmentEntity result = attachmentService.create(file);
		ArticleEntity persistedArticle = articleRepositoryFacade.findByArticleId(article.getArticleId());
		persistedArticle.getAttachments().add(result);
		articleRepositoryFacade.save(persistedArticle);
		return result;
	}

	@Transactional
	public AttachmentEntity createPersistedEntity() {
		MockMultipartFile file = newMockMultiPartFileImage();
		AttachmentEntity result = attachmentService.create(file);
		return result;
	}

	public static MockMultipartFile newMockMultiPartFileImage() {
		String filename = "image-landscape.png";
		return newMockMultiPartFileImage(MediaType.IMAGE_PNG_VALUE, filename);
	}

	public static MockMultipartFile newMockMultiPartFileImage(String mediaType, String filename) {
		MockMultipartFile result;
		try {
			InputStream imageInputStream = AttachmentHelper.class.getClassLoader().getResource("image-landscape.png").openStream();
			result = new MockMultipartFile("file", filename, mediaType, imageInputStream);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		return result;
	}

	public static MockMultipartFile newTextMockMultiPartFile(String contentType) {
		String fileName = "plain-text-multipart.txt";
		String content = "Plain text file with content type: " + contentType;
		return new MockMultipartFile("file", fileName, contentType, content.getBytes());
	}
}