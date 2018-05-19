package com.matchandtrade.rest.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class ItemFileService {

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
	private FileRepositoryFacade fileRespositoryFacade;
	
	@Transactional
	public FileEntity create(Integer itemId, MultipartFile file) {
		FileEntity result = new FileEntity();
		result.setOriginalName(StringUtils.cleanPath(file.getOriginalFilename()));
		fileRespositoryFacade.save(result);
		Path relativePath = buildRelativePath(result.getFileId());
		result.setRelativePath(relativePath.toString());
		result.setContentType(file.getContentType());
		fileRespositoryFacade.save(result);

		ItemEntity item = itemRepositoryFacade.get(itemId);
		item.getFiles().add(result);
		itemRepositoryFacade.save(item);
		
		fileStorageService.store(file, relativePath);
		return result;
	}

	private Path buildRelativePath(Integer fileId) {
		int basePath = (fileId / 100) + 100;
		return Paths.get(String.valueOf(basePath), fileId + ".file");
	}

}
