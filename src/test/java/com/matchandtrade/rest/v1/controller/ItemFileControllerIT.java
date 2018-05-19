package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.config.MatchAndTradePropertyKeys;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.FileJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ItemFileControllerIT {
	
	@Autowired
	private Environment environment;
	private Path fileStorageRootPath;
	@Autowired
	private ItemRandom itemRandom;
	private MockMultipartFile file;
	private ItemFileController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private UserRandom userRandom;

	@Before
	public void before() throws IOException {
		if (fixture == null) {
			fixture = mockControllerFactory.getFileController(false);
		}
		String fileStorageRootFolder = environment.getProperty(MatchAndTradePropertyKeys.FILE_STORAGE_ROOT_FOLDER.toString());
		fileStorageRootPath = Paths.get(fileStorageRootFolder);
		Path testFilePath = Paths.get(fileStorageRootPath.toString(), "ItemFileControllerIT.txt");
		Files.deleteIfExists(testFilePath);
		file = new MockMultipartFile(
			"test.txt",
			testFilePath.getFileName().toString(),
			MediaType.TEXT_PLAIN,
			"This is a test file for ItemFileControllerIT.java".getBytes()
		);
	}

	@Test
	public void shouldUploadItemFile() {
		TradeMembershipEntity membership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ItemEntity item = itemRandom.nextPersistedEntity(membership);
		FileJson response = fixture.post(membership.getTradeMembershipId(), item.getItemId(), file);
		Path filePath = fileStorageRootPath.resolve(response.getRelativePath());		
		assertTrue(Files.isRegularFile(filePath));
	}

	@Test
	public void shouldOnlyAllowTheItemOwnerToUploadFiles() {
		TradeMembershipEntity membership = tradeMembershipRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		ItemEntity item = itemRandom.nextPersistedEntity(membership);
		HttpStatus httpStatus = null;
		try {
			fixture.post(membership.getTradeMembershipId(), item.getItemId(), file);			
		} catch (RestException e) {
			httpStatus = e.getHttpStatus();
		}
		assertEquals(HttpStatus.FORBIDDEN, httpStatus);
	}

}
