package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.common.PersistenceUtil;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.repository.AttachmentRepository;

@Repository
public class AttachmentRepositoryFacade {
	
	@Autowired
	private AttachmentRepository attachmentRepository;
	
	public AttachmentEntity get(Integer fileId) {
		return attachmentRepository.findOne(fileId);
	}

	public void save(AttachmentEntity entity) {
		attachmentRepository.save(entity);
	}

	public void delete(Integer fileId) {
		attachmentRepository.delete(fileId);
	}

	public SearchResult<AttachmentEntity> findAttachmentsByItemId(Integer itemId, Integer pageNumber, Integer pageSize) {
		Pageable pageable = PersistenceUtil.buildPageable(pageNumber, pageSize);
		Page<AttachmentEntity> page = attachmentRepository.findAttachmentsByItemId(itemId, pageable);
		return PersistenceUtil.buildSearchResult(pageable, page);
	}
	
}
