package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.config.MatchAndTradePropertyKeys;
import com.matchandtrade.config.MvcConfiguration;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.AttachmentRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AttachmentControllerPostIT {
	
	private AttachmentController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private Environment environment;
	private Path fileStorageRootPath;
	private MockMultipartFile file;
	
	@Autowired
	private AttachmentRandom fileRandom;

	@Before
	public void before() throws IOException {
		if (fixture == null) {
			fixture = mockControllerFactory.getFileController(true);
		}
		String fileStorageRootFolder = environment.getProperty(MatchAndTradePropertyKeys.FILE_STORAGE_ROOT_FOLDER.toString());
		fileStorageRootPath = Paths.get(fileStorageRootFolder);
		file = fileRandom.newSampleMockMultiPartFile();
	}
	
	@Test
	public void shouldCreateFile() {
		AttachmentJson response = fixture.post(file);
		assertNotNull(response);
		assertNotNull(response.getAttachmentId());
		assertEquals("image/jpeg", response.getContentType());
		assertEquals(2, response.getLinks().size());
		String originalLink = response.getLinks().stream().filter(v -> "original".equals(v.getRel())).findFirst().get().getHref();
		assertNotNull(originalLink);
		Path originalFilePath = fileStorageRootPath.resolve(originalLink.replace(MvcConfiguration.FILES_URL_PATTERN.replace("*", ""), ""));
		assertTrue(originalFilePath.toFile().exists());
		String thumbnailLink = response.getLinks().stream().filter(v -> "original".equals(v.getRel())).findFirst().get().getHref();
		Path thumbnailFilePath = fileStorageRootPath.resolve(thumbnailLink.replace(MvcConfiguration.FILES_URL_PATTERN.replace("*", ""), ""));
		assertTrue(thumbnailFilePath.toFile().exists());
	}
	
}
