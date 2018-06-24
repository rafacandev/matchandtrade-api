package com.matchandtrade.test.random;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.service.AttachmentService;

@Component
public class AttachmentRandom {

	@Autowired
	private AttachmentService attachmentService;
	
	public AttachmentEntity nextPersistedEntity() {
		MultipartFile file = newSampleMockMultiPartFile();
		return attachmentService.create(file);
	}

	public MockMultipartFile newSampleMockMultiPartFile() {
		String imageResource = "image-landscape.png";
		MockMultipartFile multipartFile;
		try {
			InputStream imageInputStream = AttachmentRandom.class.getClassLoader().getResource(imageResource).openStream();
			multipartFile = new MockMultipartFile(imageResource, imageResource, "image/jpeg", imageInputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return multipartFile;
	}

}