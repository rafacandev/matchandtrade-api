package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.common.PersistenceUtil;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.persistence.repository.FileRepository;

@Repository
public class FileRepositoryFacade {
	
	@Autowired
	private FileRepository fileRepository;
	
	public FileEntity get(Integer fileId) {
		return fileRepository.findOne(fileId);
	}

	public void save(FileEntity entity) {
		fileRepository.save(entity);
	}

	public void delete(Integer fileId) {
		fileRepository.delete(fileId);
	}

	public SearchResult<FileEntity> findFilesByItemId(Integer itemId, Integer pageNumber, Integer pageSize) {
		Pageable pageable = PersistenceUtil.buildPageable(pageNumber, pageSize);
		Page<FileEntity> page = fileRepository.findFilesByItemId(itemId, pageable);
		return PersistenceUtil.buildSearchResult(pageable, page);
	}
	
}
