package com.matchandtrade.rest.service;

import com.matchandtrade.config.AppConfigurationProperties;
import com.matchandtrade.persistence.facade.EssenceRepositoryFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class EssenceStorageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EssenceStorageService.class);

	@Autowired
	private AppConfigurationProperties configProperties;
	@Autowired
	private EssenceRepositoryFacade essenceRepositoryFacade;
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

	public Path load(String filename) {
		return rootFolder.resolve(filename);
	}

	Path makeRelativePath(String filename) {
		String fileExtention;
		if (filename != null && !filename.isEmpty() && filename.lastIndexOf(".") < filename.length()) {
			fileExtention = filename.substring(filename.lastIndexOf(".")).toLowerCase();
		} else {
			fileExtention = ".file";
		}
		OffsetDateTime nowInUtc = OffsetDateTime.now(ZoneOffset.UTC);
		String pathAsString = nowInUtc.getYear() + "" + File.separatorChar + "" + nowInUtc.getMonthValue() + File.separatorChar + UUID.randomUUID() + fileExtention;
		return Paths.get(pathAsString);
	}

	public void store(byte[] bytes, Path relativePath) throws IOException {
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
			throw e;
		}
	}
}