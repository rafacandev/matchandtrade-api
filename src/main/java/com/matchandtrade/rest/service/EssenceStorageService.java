package com.matchandtrade.rest.service;

import com.matchandtrade.config.AppConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class EssenceStorageService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EssenceStorageService.class);
	
	@Autowired
	private AppConfigurationProperties configProperties;
	private Path rootFolder;

	@PostConstruct
	private void init() {
		String rootFolderProperty = configProperties.filestorage.getEssenceRootPath();
		LOGGER.info("Initiating essence service with root folder: {}", rootFolderProperty);
		Path targetRootPath = Paths.get(rootFolderProperty);
		if (!targetRootPath.toFile().isDirectory()) {
			LOGGER.error("Root folder is not a directory: {}", rootFolderProperty);
			throw new IllegalArgumentException("Root is not a directory: " + rootFolderProperty);
		}
		rootFolder = targetRootPath;
	}
	
	public void store(byte[] bytes, Path relativePath) {
		if (relativePath == null) {
			throw new IllegalArgumentException("Relative path cannot be null.");
		}
		try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
			Path parentPath = relativePath.getParent();
			if (parentPath != null && !rootFolder.resolve(parentPath).toFile().exists()) {
				Files.createDirectories(rootFolder.resolve(relativePath.getParent()));
			}
			Files.copy(inputStream, rootFolder.resolve(relativePath), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			RuntimeException ioExceptionAsRuntimeException = new RuntimeException("Failed to store essence file: " + relativePath + " at: " + rootFolder, e);
			LOGGER.error(ioExceptionAsRuntimeException.getMessage(), e);
			throw ioExceptionAsRuntimeException;
		}
	}

	public Path load(String filename) {
		return rootFolder.resolve(filename);
	}

}