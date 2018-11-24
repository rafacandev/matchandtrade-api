package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;

@Component
public class UserService {

	@Autowired
	private UserRepositoryFacade userRepository;

	@Transactional
	public void update(UserEntity user) {
		userRepository.save(user);
	}

	public UserEntity find(Integer userId) {
		return userRepository.find(userId);
	}

	public UserEntity findByArticleId(Integer articleId) {
		return userRepository.findByArticleId(articleId);
	}

	public UserEntity searchByOfferId(Integer offerId) {
		return userRepository.findByOfferId(offerId);
	}

	/**
	 * Only return {@code UserEntity.userId and UserEntity.name}
	 * if the authenticated user is requesting information from a user that is not their selves.
	 * @param requestingUser
	 * @param authenticatedUser
	 * @return
	 */
	public UserEntity sanitize(UserEntity requestingUser, UserEntity authenticatedUser) {
		if (requestingUser == null) {
			return null;
		} else if (requestingUser.getUserId().equals(authenticatedUser.getUserId())) {
			return requestingUser;
		} else {			
			UserEntity result = new UserEntity();
			result.setUserId(requestingUser.getUserId());
			result.setName(requestingUser.getName());
			return result;
		}
	}

}
