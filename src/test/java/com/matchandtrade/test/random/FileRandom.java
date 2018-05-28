package com.matchandtrade.test.random;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.rest.service.FileService;

@Component
public class FileRandom {

	@Autowired
	private FileService fileService;
	
	public FileEntity nextPersistedEntity() {
		MultipartFile file = newSampleMockMultiPartFile();
		return fileService.create(file);
	}

	public MockMultipartFile newSampleMockMultiPartFile() {
		String imageResource = "image-landscape.png";
		MockMultipartFile file;
		try {
			InputStream imageInputStream = FileRandom.class.getClassLoader().getResource(imageResource).openStream();
			file = new MockMultipartFile(imageResource, imageResource, "image/jpeg", imageInputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return file;
	}

}