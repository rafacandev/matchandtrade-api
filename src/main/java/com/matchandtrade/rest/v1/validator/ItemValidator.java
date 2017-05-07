package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.repository.TradeMembershipRepository;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ItemJson;

@Component
public class ItemValidator {

	@Autowired
	TradeMembershipRepository tradeMembershipRepository;

	/**
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code tradeMembershipId}
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code json.name} is null or length is not between 3 and 150. 
	 * @param userId
	 * @param tradeMembershipId
	 * @param json
	 */
	public void validatePost(Integer userId, Integer tradeMembershipId, ItemJson json) {
		TradeMembershipEntity tradeMembershipEntity = tradeMembershipRepository.get(tradeMembershipId);
		if (!userId.equals(tradeMembershipEntity.getUser().getUserId())) {
			throw new RestException(HttpStatus.FORBIDDEN, "Authenticated user is not associated to TradeMembership.tradeMembershipId: " + tradeMembershipId);
		}
		if (json.getName() == null || json.getName().length() < 3 || json.getName().length() > 150) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Item.name is mandatory and must be between 3 and 150 characters in length.");
		}
	}

}
