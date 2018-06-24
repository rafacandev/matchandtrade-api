package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.persistence.facade.ItemRepositoryFacade;

@Service
public class ItemAttachmentService {
	
	@Autowired
	private ItemRepositoryFacade itemRepositoryFacade;
	@Autowired
	private AttachmentRepositoryFacade attachmentRepositoryFacade;

	@Transactional
	public void addAttachmentToItem(Integer itemId, Integer attachmentId) {
		AttachmentEntity attachment = attachmentRepositoryFacade.get(attachmentId);
		ItemEntity item = itemRepositoryFacade.get(itemId);
		item.getAttachments().add(attachment);
		itemRepositoryFacade.save(item);
	}

	public SearchResult<AttachmentEntity> search(Integer itemId, Integer pageNumber, Integer pageSize) {
		return attachmentRepositoryFacade.findAttachmentsByItemId(itemId, pageNumber, pageSize);
	}

	@Transactional
	public void deleteAttachmentFromItem(Integer itemId, Integer attachmentId) {
		AttachmentEntity attachment = attachmentRepositoryFacade.get(attachmentId);
		ItemEntity item = itemRepositoryFacade.get(itemId);
		if (item.getAttachments().remove(attachment)) {
			itemRepositoryFacade.save(item);
			attachmentRepositoryFacade.delete(attachmentId);
		}
	}

}
