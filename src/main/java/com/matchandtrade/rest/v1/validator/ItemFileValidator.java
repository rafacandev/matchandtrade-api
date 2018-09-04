package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.rest.RestException;

@Component
public class ItemFileValidator {
	
	@Autowired
	private ItemValidator itemValidator;
	@Autowired
	private ArticleRepositoryFacade itemRepositoryFacade;
	@Autowired
	private AttachmentRepositoryFacade fileRespositoryFacade;
	
	public void validateDelete(Integer userId, Integer tradeMembershipId, Integer articleId, Integer fileId) {
		itemValidator.validateOwnership(userId, tradeMembershipId);
		AttachmentEntity file = fileRespositoryFacade.get(fileId);
		if (file == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "There is no File for the given File.fileId.");
		}
		
	}
	
	/**
	 * Same as in {@link com.matchandtrade.rest.v1.validator.ItemValidator.validateOwnership()}.
	 * Also validates if the target {@code Item} for the given {@code articleId} has less than two files.
	 * 
	 * @param userId
	 * @param tradeMembershipId
	 * @param articleId
	 */
	@Transactional
	public void validatePost(Integer userId, Integer tradeMembershipId, Integer articleId) {
		itemValidator.validateOwnership(userId, tradeMembershipId);
		validateThatItemHasLessThanTwoFiles(articleId);
	}

	private void validateThatItemHasLessThanTwoFiles(Integer articleId) {
		ArticleEntity item = itemRepositoryFacade.get(articleId);
		if (item.getAttachments().size() > 2) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Items cannot have more than 3 files.");
		}
	}

	public void validateGet(Integer userId, Integer tradeMembershipId, Integer pageNumber, Integer pageSize) {
		PaginationValidator.validatePageNumberAndPageSize(pageNumber, pageSize);
	}

}
