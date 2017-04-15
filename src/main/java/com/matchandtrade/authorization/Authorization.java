package com.matchandtrade.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.repository.UserRespository;


@Component
public class Authorization {

	@Autowired
	private UserRespository userRepository;
	
	/**
	 * Throws {@code AuthorizationException} if {@code authenticationEntity} or {@code authenticationEntity.getUserId()} is null
	 * Throws {@code AuthorizationException} if there is not @ {@code UserEntity} for {@code authenticationEntity.getUserId()}.
	 * @param authenticationEntity
	 */
	public void validateIdentity(AuthenticationEntity authenticationEntity) {
		if (authenticationEntity == null || authenticationEntity.getUserId() == null) {
			throw new AuthorizationException(AuthorizationException.Type.UNAUTHORIZED);
		}
		UserEntity userEntity = userRepository.get(authenticationEntity.getUserId());
		if (userEntity == null) {
			throw new AuthorizationException(AuthorizationException.Type.FORBIDDEN);
		}
	}

}
