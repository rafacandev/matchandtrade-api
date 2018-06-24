package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.persistence.facade.ItemRepositoryFacade;
import com.matchandtrade.rest.RestException;

@Component
public class ItemFileValidator {
	
	@Autowired
	private ItemValidator itemValidator;
	@Autowired
	private ItemRepositoryFacade itemRepositoryFacade;
	@Autowired
	private AttachmentRepositoryFacade fileRespositoryFacade;
	
	public void validateDelete(Integer userId, Integer tradeMembershipId, Integer itemId, Integer fileId) {
		itemValidator.validateOwnership(userId, tradeMembershipId);
		AttachmentEntity file = fileRespositoryFacade.get(fileId);
		if (file == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "There is no File for the given File.fileId.");
		}
		
	}
	
	/**
	 * Same as in {@link com.matchandtrade.rest.v1.validator.ItemValidator.validateOwnership()}.
	 * Also validates if the target {@code Item} for the given {@code itemId} has less than two files.
	 * 
	 * @param userId
	 * @param tradeMembershipId
	 * @param itemId
	 */
	@Transactional
	public void validatePost(Integer userId, Integer tradeMembershipId, Integer itemId) {
		itemValidator.validateOwnership(userId, tradeMembershipId);
		validateThatItemHasLessThanTwoFiles(itemId);
	}

	private void validateThatItemHasLessThanTwoFiles(Integer itemId) {
		ItemEntity item = itemRepositoryFacade.get(itemId);
		if (item.getAttachments().size() > 2) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Items cannot have more than 3 files.");
		}
	}

	public void validateGet(Integer userId, Integer tradeMembershipId, Integer pageNumber, Integer pageSize) {
		PaginationValidator.validatePageNumberAndPageSize(pageNumber, pageSize);
	}

}
