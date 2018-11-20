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

import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.EssenceEntity;
import com.matchandtrade.persistence.entity.EssenceEntity.Type;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.persistence.facade.EssenceRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.util.ImageUtil;

@Service
public class AttachmentService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentService.class);

	@Autowired
	private EssenceStorageService essenceStorageService;
	@Autowired
	private AttachmentRepositoryFacade attachmentRepositoryFacade;
	@Autowired
	private EssenceRepositoryFacade essenceRepositoryFacade;
	
	private final int THUMBNAIL_SIZE = 128;
	
	// TODO: Potentially move this to FileStorageService
	private Path buildNewRelativePath(String filename) {
		String fileExtention = ".file";
		if (filename != null && !filename.isEmpty() && filename.lastIndexOf(".") < filename.length()) {
			fileExtention = filename.substring(filename.lastIndexOf(".")).toLowerCase();
		}
		LocalDate now = LocalDate.now();
		String pathAsString = now.getYear() + "" + File.separatorChar + "" + now.getMonthValue() + File.separatorChar + UUID.randomUUID().toString() + fileExtention;
		return Paths.get(pathAsString);
	}

	@Transactional
	public AttachmentEntity create(MultipartFile multipartFile) {
		Path originalEssenceRelativePath = buildNewRelativePath(multipartFile.getOriginalFilename());
		LOGGER.debug("Storing original essence file at: {}", originalEssenceRelativePath);
		storeFileOnFileSystem(multipartFile, originalEssenceRelativePath);

		LOGGER.debug("Saving original essence entity");
		EssenceEntity originalEssence = new EssenceEntity();
		originalEssence.setType(Type.ORIGINAL);
		originalEssence.setRelativePath(originalEssenceRelativePath.toString());
		essenceRepositoryFacade.save(originalEssence);
		
		LOGGER.debug("Saving AttachmentEntity with original EssenceEntity");
		AttachmentEntity result = new AttachmentEntity();
		result.setContentType(multipartFile.getContentType());
		result.setName(multipartFile.getOriginalFilename());
		result.getEssences().add(originalEssence);
		attachmentRepositoryFacade.save(result);

		if (multipartFile.getContentType() != null && multipartFile.getContentType().contains("image")) {
			Path thumbnailRelativePath = buildNewRelativePath(multipartFile.getOriginalFilename());
			LOGGER.debug("Attempting to generate thumbnail for content type: {}; from essence file: {}; to thumbnail file: {}", multipartFile.getContentType(), originalEssenceRelativePath, thumbnailRelativePath);
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
		Path fullPath = essenceStorageService.load(relativePath.toString());
		Image image = ImageIO.read(fullPath.toFile());
		if (image == null) {
			throw new IllegalArgumentException("Could not read file as image.");
		}
		Image imageResized = ImageUtil.obtainShortEdgeResizedImage(image, THUMBNAIL_SIZE);
		return ImageUtil.obtainCenterCrop(imageResized, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
	}
	
	public AttachmentEntity find(Integer attachmentId) {
		return attachmentRepositoryFacade.find(attachmentId);
	}

	private void saveThumbnailEssence(AttachmentEntity attachment, Path thumbnailRelativePath) {
		EssenceEntity thumbnailEssence = new EssenceEntity();
		thumbnailEssence.setRelativePath(thumbnailRelativePath.toString());
		thumbnailEssence.setType(Type.THUMBNAIL);
		essenceRepositoryFacade.save(thumbnailEssence);
		LOGGER.debug("Saving AttachmentEntity with thumbnail EssenceEntity");
		attachment.getEssences().add(thumbnailEssence);
		attachmentRepositoryFacade.save(attachment);
	}
	
	private void storeFileOnFileSystem(MultipartFile multipartFile, Path relativePath) {
		try {
			essenceStorageService.store(multipartFile.getBytes(), relativePath);
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
				essenceStorageService.store(thumbnailOuputStream.toByteArray(), thumbnailRelativePath);
			}
		}
	}

}
