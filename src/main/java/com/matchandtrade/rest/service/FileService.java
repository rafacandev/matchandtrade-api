package com.matchandtrade.rest.service;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.matchandtrade.persistence.entity.EssenceEntity;
import com.matchandtrade.persistence.entity.EssenceEntity.Type;
import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.persistence.facade.EssenceRepositoryFacade;
import com.matchandtrade.persistence.facade.FileRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.util.ImageUtil;

@Service
public class FileService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemFileService.class);

	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private FileRepositoryFacade fileRepositoryFacade;
	@Autowired
	private EssenceRepositoryFacade essenceRepositoryFacade;
	
	private final int thumbnailSize = 128;
	
	// TODO: Potentially move this to FileStorageService
	private Path buildNewRelativePath() {
		LocalDate now = LocalDate.now();
		String pathAsString = now.getYear() + "" + File.separatorChar + "" + now.getMonthValue() + File.separatorChar + UUID.randomUUID().toString() + ".file";
		return Paths.get(pathAsString);
	}

	@Transactional
	public FileEntity create(MultipartFile file) {
		Path originalEssenceRelativePath = buildNewRelativePath();
		LOGGER.debug("Storing original essence file at: {}", originalEssenceRelativePath);
		storeFileOnFileSystem(file, originalEssenceRelativePath);

		LOGGER.debug("Saving original essence entity");
		EssenceEntity originalEssence = new EssenceEntity();
		originalEssence.setType(Type.ORIGINAL);
		originalEssence.setRelativePath(originalEssenceRelativePath.toString());
		essenceRepositoryFacade.save(originalEssence);
		
		LOGGER.debug("Saving file entity with original essence entity");
		FileEntity result = new FileEntity();
		result.setContentType(file.getContentType());
		result.setName(file.getOriginalFilename());
		result.getEssences().add(originalEssence);
		fileRepositoryFacade.save(result);

		if (file.getContentType().contains("image")) {
			Path thumbnailRelativePath = buildNewRelativePath();
			LOGGER.debug("Attempting to generate thumbnail for content type: {}; from essence file: {}; to thumbnail file: {}", file.getContentType(), originalEssenceRelativePath, thumbnailRelativePath);
			try {
				storeThumbnailOnFileSystem(originalEssenceRelativePath, thumbnailRelativePath);
				LOGGER.debug("Saving thumbnail essence entity");
				saveThumbnailEssence(result, thumbnailRelativePath);
			} catch (IOException | IllegalArgumentException e) {
				LOGGER.warn("Unable to store and save thumbnail, proceding without a thumbnail. {}", e.getMessage());
			}
		}
		return result;
	}

	private Image createThumbnailImage(Path relativePath) throws IOException {
		Path fullPath = fileStorageService.load(relativePath.toString());
		Image image = ImageIO.read(fullPath.toFile());
		if (image == null) {
			throw new IllegalArgumentException("Could not read file as image.");
		}
		Image imageResized = ImageUtil.obtainShortEdgeResizedImage(image, thumbnailSize);
		return ImageUtil.obtainCenterCrop(imageResized, thumbnailSize, thumbnailSize);
	}
	
	public FileEntity get(Integer fileId) {
		return fileRepositoryFacade.get(fileId);
	}

	private void saveThumbnailEssence(FileEntity result, Path thumbnailRelativePath) {
		EssenceEntity thumbnailEssence = new EssenceEntity();
		thumbnailEssence.setRelativePath(thumbnailRelativePath.toString());
		thumbnailEssence.setType(Type.THUMBNAIL);
		essenceRepositoryFacade.save(thumbnailEssence);
		LOGGER.debug("Saving file entity with thumbnail essence entity");
		result.getEssences().add(thumbnailEssence);
		fileRepositoryFacade.save(result);
	}
	
	private void storeFileOnFileSystem(MultipartFile file, Path relativePath) {
		try {
			fileStorageService.store(file.getBytes(), relativePath);
		} catch (IOException e) {
			LOGGER.error("Unable to ready file", e);
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to ready file. " + e.getMessage());
		}
	}

	private void storeThumbnailOnFileSystem(Path originalEssenceRelativePath, Path thumbnailRelativePath) throws IOException {
		Image thumbnailImage = createThumbnailImage(originalEssenceRelativePath);
		if (thumbnailImage != null) {
			try (ByteArrayOutputStream thumbnailOuputStream = new ByteArrayOutputStream()) {
				ImageIO.write(ImageUtil.buildBufferedImage(thumbnailImage), "JPG", thumbnailOuputStream);
				fileStorageService.store(thumbnailOuputStream.toByteArray(), thumbnailRelativePath);
			}
		}
	}

}
