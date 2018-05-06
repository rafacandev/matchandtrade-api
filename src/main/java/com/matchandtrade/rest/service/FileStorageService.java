package com.matchandtrade.rest.service;

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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
		if (!Files.exists(targetRootPath) || !Files.isDirectory(targetRootPath)) {
			LOGGER.error("Root folder does not exist or is not a directory: {}", rootFolderProperty);
			throw new IllegalArgumentException("Root folder does not exist or is not a directory: " + rootFolderProperty);
		}
		rootFolder = targetRootPath;
	}
	
	public void store(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (file.isEmpty()) {
				throw new IllegalArgumentException("Failed to store empty file " + filename);
			}
			// Do not store filenames starting with .. as would cause it to be stored on the parent directory
			if (filename.contains("..")) {
				throw new IllegalArgumentException("Cannot store file when filename starts with '..'" + filename);
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, rootFolder.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			RuntimeException unknownException = new RuntimeException("Failed to store file: " + filename + " at: " + rootFolder, e);
			LOGGER.error(unknownException.getMessage(), e);
			throw unknownException;
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
			throw new RuntimeException("Could not read file: " + filename + ". " + e.getMessage());
		}
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootFolder.toFile());
	}

}