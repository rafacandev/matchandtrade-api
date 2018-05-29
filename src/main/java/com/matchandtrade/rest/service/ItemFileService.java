package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.facade.FileRepositoryFacade;
import com.matchandtrade.persistence.facade.ItemRepositoryFacade;

@Service
public class ItemFileService {
	
	@Autowired
	private ItemRepositoryFacade itemRepositoryFacade;
	@Autowired
	private FileRepositoryFacade fileRepositoryFacade;

	@Transactional
	public void addFileToItem(Integer itemId, Integer fileId) {
		FileEntity file = fileRepositoryFacade.get(fileId);
		ItemEntity item = itemRepositoryFacade.get(itemId);
		item.getFiles().add(file);
		itemRepositoryFacade.save(item);
	}

	public SearchResult<FileEntity> search(Integer itemId, Integer pageNumber, Integer pageSize) {
		return itemRepositoryFacade.findFilesByItemId(itemId, pageNumber, pageSize);
	}

}
