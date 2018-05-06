package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemFileValidator {
	
	@Autowired
	private ItemValidator itemValidator;
	
	/**
	 * Same as in {@link com.matchandtrade.rest.v1.validator.ItemValidator.validateOwnership()} 
	 * @param userId
	 * @param tradeMembershipId
	 */
	public void validatePost(Integer userId, Integer tradeMembershipId) {
		itemValidator.validateOwnership(userId, tradeMembershipId);
	}
}
