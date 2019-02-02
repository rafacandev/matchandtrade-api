package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.EssenceEntity;
import com.matchandtrade.persistence.entity.EssenceEntity.Type;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class AttachmentService {
	private final Logger log = LoggerFactory.getLogger(AttachmentService.class);
	private final int THUMBNAIL_SIZE = 128;

	@Autowired
	private EssenceStorageService essenceStorageService;
	@Autowired
	private EssenceService essenceService;
	@Autowired
	private AttachmentRepositoryFacade attachmentRepositoryFacade;

	@Transactional
	public AttachmentEntity create(MultipartFile multipartFile) {
		Path originalEssenceRelativePath = essenceStorageService.makeRelativePath(multipartFile.getOriginalFilename());
		log.debug("Storing original essence file at: {}", originalEssenceRelativePath);
		storeFileOnFileSystem(multipartFile, originalEssenceRelativePath);

		log.debug("Saving original essence entity");
		EssenceEntity originalEssence = new EssenceEntity();
		originalEssence.setType(Type.ORIGINAL);
		originalEssence.setRelativePath(originalEssenceRelativePath.toString());
		essenceService.save(originalEssence);
		
		log.debug("Saving AttachmentEntity with original EssenceEntity");
		AttachmentEntity result = new AttachmentEntity();
		result.setContentType(multipartFile.getContentType());
		result.setName(multipartFile.getOriginalFilename());
		result.getEssences().add(originalEssence);
		attachmentRepositoryFacade.save(result);

		if (multipartFile.getContentType() != null && multipartFile.getContentType().contains("image")) {
			Path thumbnailRelativePath = essenceStorageService.makeRelativePath(multipartFile.getOriginalFilename());
			log.debug("Attempting to generate thumbnail for content type: {}; from essence file: {}; to thumbnail file: {}", multipartFile.getContentType(), originalEssenceRelativePath, thumbnailRelativePath);
			try {
				storeThumbnailOnFileSystem(originalEssenceRelativePath, thumbnailRelativePath);
				log.debug("Saving thumbnail essence entity");
				saveThumbnailEssence(result, thumbnailRelativePath);
			} catch (IOException | IllegalArgumentException e) {
				log.warn("Unable to save thumbnail, proceeding without a thumbnail. {}", e.getMessage());
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

	public void delete(UUID attachmentId) {
		attachmentRepositoryFacade.delete(attachmentId);
	}

	public SearchResult<AttachmentEntity> findByArticleId(Integer articleId) {
		return attachmentRepositoryFacade.findByArticleId(articleId);
	}

	public AttachmentEntity findByAttachmentId(UUID attachmentId) {
		return attachmentRepositoryFacade.findByAttachmentId(attachmentId);
	}

	private void saveThumbnailEssence(AttachmentEntity attachment, Path thumbnailRelativePath) {
		EssenceEntity thumbnailEssence = new EssenceEntity();
		thumbnailEssence.setRelativePath(thumbnailRelativePath.toString());
		thumbnailEssence.setType(Type.THUMBNAIL);
		essenceService.save(thumbnailEssence);
		log.debug("Saving AttachmentEntity with thumbnail EssenceEntity");
		attachment.getEssences().add(thumbnailEssence);
		attachmentRepositoryFacade.save(attachment);
	}
	
	private void storeFileOnFileSystem(MultipartFile multipartFile, Path relativePath) {
		try {
			essenceStorageService.store(multipartFile.getBytes(), relativePath);
		} catch (IOException e) {
			log.error("Unable to store file", e);
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to store file. " + e.getMessage());
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
