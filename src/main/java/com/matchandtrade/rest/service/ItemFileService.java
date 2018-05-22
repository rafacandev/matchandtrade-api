package com.matchandtrade.rest.service;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ItemQueryBuilder;
import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.facade.FileRepositoryFacade;
import com.matchandtrade.persistence.facade.ItemRepositoryFacade;
import com.matchandtrade.persistence.facade.TradeMembershipRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.util.ImageUtil;

@Service
public class ItemFileService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemFileService.class);

	@Autowired
	private TradeMembershipRepositoryFacade tradeMembershipRepositoryFacade;
	@Autowired
	private ItemRepositoryFacade itemRepositoryFacade;
	
	
	@Autowired
	private SearchService searchService;

	@Transactional
	public void create(Integer tradeMembershipId, ItemEntity itemEntity) {
		TradeMembershipEntity tradeMembershipEntity = tradeMembershipRepositoryFacade.get(tradeMembershipId);
		itemRepositoryFacade.save(itemEntity);
		tradeMembershipEntity.getItems().add(itemEntity);
		tradeMembershipRepositoryFacade.save(tradeMembershipEntity);
	}

	@Transactional
	public void delete(Integer tradeMembershipId, Integer itemId) {
		TradeMembershipEntity membership = tradeMembershipRepositoryFacade.get(tradeMembershipId);
		ItemEntity item = itemRepositoryFacade.get(itemId);
		membership.getItems().remove(item);
		tradeMembershipRepositoryFacade.save(membership);
		itemRepositoryFacade.delete(itemId);
	}

	public ItemEntity get(Integer itemId) {
		return itemRepositoryFacade.get(itemId);
	}
	
	public boolean exists(Integer ...itemIds) {
		return itemRepositoryFacade.exists(itemIds);
	}

	@Transactional
	public SearchResult<ItemEntity> searchByTradeMembershipIdName(Integer tradeMembershipId, Integer _pageNumber, Integer _pageSize) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		searchCriteria.addCriterion(ItemQueryBuilder.Field.tradeMembershipId, tradeMembershipId);
		return searchService.search(searchCriteria, ItemQueryBuilder.class);
	}

	public void update(ItemEntity itemEntity) {
		itemRepositoryFacade.save(itemEntity);
	}

	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private FileRepositoryFacade fileRepositoryFacade;
	
	@Transactional
	public FileEntity create(Integer itemId, MultipartFile file) {
		FileEntity result = new FileEntity();
		result.setOriginalName(StringUtils.cleanPath(file.getOriginalFilename()));
		fileRepositoryFacade.save(result);
		Path relativePath = buildRelativePath(result.getFileId());
		result.setRelativePath(relativePath.toString());
		result.setContentType(file.getContentType());

		// Store uploaded file
		byte[] fileBytes;
		try {
			fileBytes = file.getBytes();
		} catch (IOException e) {
			LOGGER.error("Unable to ready file", e);
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to ready file. " + e.getMessage());
		}
		fileStorageService.store(fileBytes, relativePath);
		
		FileEntity thumbnail = createThumbnail(fileStorageService.load(result.getRelativePath()).toFile());
		result.getRelatedFiles().put("THUMBNAIL", thumbnail);

		fileRepositoryFacade.save(result);
		ItemEntity item = itemRepositoryFacade.get(itemId);
		item.getFiles().add(result);
		itemRepositoryFacade.save(item);

		return result;
	}

	private FileEntity createThumbnail(File file) {
		FileEntity result = new FileEntity();
		try {
			// Generate thumbnail
			Image image = ImageIO.read(file);
			Image imageResized = ImageUtil.obtainShortEdgeResizedImage(image, 128);
			Image imageThumbnail = ImageUtil.obtainCenterCrop(imageResized, 128, 128);
			
			fileRepositoryFacade.save(result);
			Path relativePath = buildRelativePath(result.getFileId());
			result.setRelativePath(relativePath.toString());
			result.setContentType("image/jpeg");
			fileRepositoryFacade.save(result);
			
			ByteArrayOutputStream thumbnailOuputStream = new ByteArrayOutputStream();
			ImageIO.write(ImageUtil.buildBufferedImage(imageThumbnail), "JPG", thumbnailOuputStream);
			fileStorageService.store(thumbnailOuputStream.toByteArray(), relativePath);
		} catch (IOException e) {
			LOGGER.warn("Unable to generate thubnail file.", e);
		}
		return result;
	}

	private Path buildRelativePath(Integer fileId) {
		int basePath = (fileId / 100) + 1;
		return Paths.get(String.valueOf(basePath), fileId + ".file");
	}

}
