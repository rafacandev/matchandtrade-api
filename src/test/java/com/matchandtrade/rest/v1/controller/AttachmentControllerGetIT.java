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
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.config.MatchAndTradePropertyKeys;
import com.matchandtrade.config.MvcConfiguration;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.AttachmentRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AttachmentControllerGetIT {
	
	@Autowired
	private AttachmentRandom attachmentRandom;
	@Autowired
	private Environment environment;
	private AttachmentController fixture;
	private Path fileStorageRootPath;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	
	@Before
	public void before() throws IOException {
		if (fixture == null) {
			fixture = mockControllerFactory.getFileController(true);
		}
		String fileStorageRootFolder = environment.getProperty(MatchAndTradePropertyKeys.ESSENCE_STORAGE_ROOT_FOLDER.toString());
		fileStorageRootPath = Paths.get(fileStorageRootFolder);
	}
	
	@Test
	public void shouldGetAttachment() {
		AttachmentEntity entity = attachmentRandom.nextPersistedEntity();
		AttachmentJson response = fixture.get(entity.getAttachmentId());
		assertNotNull(response);
		assertEquals(entity.getAttachmentId(), response.getAttachmentId());
		assertEquals(entity.getContentType(), response.getContentType());
		assertEquals(3, response.getLinks().size());
		assertEquals(entity.getName(), response.getName());
		
		String originalLink = response.getLinks().stream().filter(v -> "original".equals(v.getRel())).findFirst().get().getHref();
		assertNotNull(originalLink);
		Path originalFilePath = fileStorageRootPath.resolve(originalLink.replace(MvcConfiguration.ESSENCES_URL_PATTERN.replace("*", ""), ""));
		assertTrue(originalFilePath.toFile().exists());
		String thumbnailLink = response.getLinks().stream().filter(v -> "original".equals(v.getRel())).findFirst().get().getHref();
		Path thumbnailFilePath = fileStorageRootPath.resolve(thumbnailLink.replace(MvcConfiguration.ESSENCES_URL_PATTERN.replace("*", ""), ""));
		assertTrue(thumbnailFilePath.toFile().exists());
	}

	@Test(expected = RestException.class)
	public void shouldErrorWhenAttachmentIdIsInvalid() {
		fixture.get(-1);
	}

}
