package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;

@Component
public class UserService {

	@Autowired
	private UserRepositoryFacade userRepositoryFacade;

	@Transactional
	public void update(UserEntity user) {
		userRepositoryFacade.save(user);
	}

	public UserEntity findByArticleId(Integer articleId) {
		return userRepositoryFacade.findByArticleId(articleId);
	}

	public UserEntity findByOfferId(Integer offerId) {
		return userRepositoryFacade.findByOfferId(offerId);
	}

	public UserEntity findByUserId(Integer userId) {
		return userRepositoryFacade.findByUserId(userId);
	}

	/**
	 * Only return {@code UserEntity.userId and UserEntity.name} when {@code requestingUser}
	 * and {@code authenticatedUser} do not have the same {@code UserEntity.userId}
	 *
	 * @param authenticatedUser
	 * @param requestingUser
	 * @return
	 */
	public UserEntity sanitize(UserEntity authenticatedUser, UserEntity requestingUser) {
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

	public void save(UserEntity user) {
		userRepositoryFacade.save(user);
	}
}
