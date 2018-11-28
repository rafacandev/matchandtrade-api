package com.matchandtrade.rest.service;

import com.matchandtrade.config.AppConfigurationProperties;
import com.matchandtrade.test.DefaultTestingConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class EssenceStorageServiceIT {

	@Autowired
	private AppConfigurationProperties configProperties;
	@Autowired
	private EssenceStorageService fixture;

	@Test
	public void store_When_PathIsValid_Then_Succeeds() throws IOException {
		String rootFolderProperty = configProperties.filestorage.getEssenceRootPath();
		String givenString = "EssenceStorageServiceIT as plain text file";

		Path givenPath = Paths.get("essence-storage-test" + File.separator + System.currentTimeMillis() + ".txt");
		fixture.store(givenString.getBytes(), givenPath);
		Path actualPath = fixture.load(givenPath.toString());

		byte[] actualBytes = Files.readAllBytes(actualPath);
		String actualString = new String(actualBytes);

		assertEquals(rootFolderProperty + givenPath.toString(), actualPath.toString());
		assertEquals(givenString, actualString);
	}

	@Test(expected = IllegalArgumentException.class)
	public void store_When_PathIsNull_Then_IllegalArgumentException() throws IOException {
		fixture.store("this-test-should-fail".getBytes(), null);
	}

}
