package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.repository.UserRepository;

@Repository
public class UserRepositoryFacade {
	
	@Autowired
	private UserRepository userRepository;

	// TODO: Optional ??
	public UserEntity find(Integer userId) {
		return userRepository.findById(userId).get();
	}

	public UserEntity findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void save(UserEntity entity) {
		userRepository.save(entity);
	}

	public UserEntity findByArticleId(Integer articleId) {
		return userRepository.findByArticleId(articleId);
	}

	public UserEntity findByOfferId(Integer offerId) {
		return userRepository.findByOfferId(offerId);
	}

}
