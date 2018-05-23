package com.matchandtrade.rest.service;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.facade.EssenceRepositoryFacade;
import com.matchandtrade.persistence.facade.FileRepositoryFacade;
import com.matchandtrade.persistence.facade.ItemRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.util.ImageUtil;

@Service
public class ItemFileService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemFileService.class);

	@Autowired
	private ItemRepositoryFacade itemRepositoryFacade;
	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private FileRepositoryFacade fileRepositoryFacade;
	@Autowired
	private EssenceRepositoryFacade essenceRepositoryFacade;
	
	private Path buildRelativePath(EssenceEntity essence) {
		int basePath = (essence.getEssenceId() / 100) + 1;
		return Paths.get(String.valueOf(basePath), essence.getEssenceId() + ".file");
	}

	@Transactional
	public FileEntity create(Integer itemId, MultipartFile file) {
		// Save original essence entity
		EssenceEntity originalEssence = new EssenceEntity();
		originalEssence.setType(Type.ORIGINAL);
		essenceRepositoryFacade.save(originalEssence);
		Path originalEssenceRelativePath = buildRelativePath(originalEssence);
		// Store the original file
		storeFile(file, originalEssenceRelativePath);
		originalEssence.setRelativePath(originalEssenceRelativePath.toString());
		essenceRepositoryFacade.save(originalEssence);
		// Save file entity
		FileEntity result = new FileEntity();
		result.setContentType(file.getContentType());
		result.getEssences().add(originalEssence);
		result.setOriginalName(file.getOriginalFilename());
		fileRepositoryFacade.save(result);
		// Save the thumbnail essence
		EssenceEntity thumbnailEssence = saveAndStoreThumbnail(originalEssenceRelativePath);
		if (thumbnailEssence != null) {
			result.getEssences().add(thumbnailEssence);
		}
		// Assign the file entity to its item entity
		ItemEntity item = itemRepositoryFacade.get(itemId);
		item.getFiles().add(result);
		itemRepositoryFacade.save(item);
		return result;
	}
	
	/**
	 * Save thumbnail essence entity 
	 * @param originalEssence
	 * @param originalEssenceRelativePath
	 * @param result
	 */
	private EssenceEntity saveAndStoreThumbnail(Path originalEssenceRelativePath) {
		Image thumbnailImage = null;
		EssenceEntity result = null;
		try {
			thumbnailImage = createThumbnailImage(originalEssenceRelativePath);
		} catch (IllegalArgumentException e) {
			LOGGER.warn("Unable to create thumbnail for the original essence located at: {}", originalEssenceRelativePath);
		}
		
		if (thumbnailImage != null) {
			EssenceEntity thumbnailEssence = new EssenceEntity();
			try (ByteArrayOutputStream thumbnailOuputStream = new ByteArrayOutputStream()) {
				ImageIO.write(ImageUtil.buildBufferedImage(thumbnailImage), "JPG", thumbnailOuputStream);
				thumbnailEssence.setType(Type.THUMBNAIL);
				essenceRepositoryFacade.save(thumbnailEssence);
				Path thumbnailEssenceRelativePath = buildRelativePath(thumbnailEssence);
				fileStorageService.store(thumbnailOuputStream.toByteArray(), thumbnailEssenceRelativePath);			
				thumbnailEssence.setRelativePath(thumbnailEssenceRelativePath.toString());
				essenceRepositoryFacade.save(thumbnailEssence);
				result = thumbnailEssence;
			} catch (IOException e) {
				LOGGER.warn("Unable to write thumbnail OutputStream for the original essence located at: {}", originalEssenceRelativePath);
				// Remove the possible orphan essence if something goes wrong
				if (thumbnailEssence.getEssenceId() != null) {
					essenceRepositoryFacade.delete(thumbnailEssence.getEssenceId());
				}
			}
		}
		return result;
	}

	private Image createThumbnailImage(Path relativePath) {
		Image result;
		try {
			Path fullPath = fileStorageService.load(relativePath.toString());
			Image image = ImageIO.read(fullPath.toFile());
			Image imageResized = ImageUtil.obtainShortEdgeResizedImage(image, 128);
			result = ImageUtil.obtainCenterCrop(imageResized, 128, 128);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return result;
	}

	private void storeFile(MultipartFile file, Path relativePath) {
		try {
			fileStorageService.store(file.getBytes(), relativePath);
		} catch (IOException e) {
			LOGGER.error("Unable to ready file", e);
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to ready file. " + e.getMessage());
		}
	}

}
