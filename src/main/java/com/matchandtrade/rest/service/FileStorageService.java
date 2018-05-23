package com.matchandtrade.rest.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import com.matchandtrade.config.MatchAndTradePropertyKeys;

@Service
public class FileStorageService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);
	
	@Autowired
	private Environment environment;
	private Path rootFolder;

	@PostConstruct
	private void init() {
		String rootFolderProperty = environment.getProperty(MatchAndTradePropertyKeys.FILE_STORAGE_ROOT_FOLDER.toString()); 
		LOGGER.info("Initiating file service with root folder: {}", rootFolderProperty);
		Path targetRootPath = Paths.get(rootFolderProperty);
		if (!targetRootPath.toFile().isDirectory()) {
			LOGGER.error("Root folder is not a directory: {}", rootFolderProperty);
			throw new IllegalArgumentException("Root is not a directory: " + rootFolderProperty);
		}
		rootFolder = targetRootPath;
	}
	
	public void store(byte[] bytes, Path relativePath) {
		InputStream inputStream = new ByteArrayInputStream(bytes);
		try {
			if (relativePath == null) {
				throw new IllegalArgumentException("Relative path cannot be null.");
			}
			if (!rootFolder.resolve(relativePath.getParent()).toFile().exists()) {					
				Files.createDirectories(rootFolder.resolve(relativePath.getParent()));
			}
			Files.copy(inputStream, rootFolder.resolve(relativePath), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			RuntimeException ioExceptionAsRuntimeException = new RuntimeException("Failed to store file: " + relativePath + " at: " + rootFolder, e);
			LOGGER.error(ioExceptionAsRuntimeException.getMessage(), e);
			throw ioExceptionAsRuntimeException;
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				LOGGER.error("Not possible to close input stream", e);
			}
		}
	}

	public Path load(String filename) {
		return rootFolder.resolve(filename);
	}

	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read file: " + filename);

			}
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Could not read file: " + filename + ". " + e.getMessage());
		}
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootFolder.toFile());
	}

}