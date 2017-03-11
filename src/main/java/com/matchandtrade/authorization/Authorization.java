package com.matchandtrade.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;


@Component
public class Authorization {

	@Autowired
	private UserModel userModel;
	
	/**
	 * Throws {@code AuthorizationException} if {@code authenticationEntity} or {@code authenticationEntity.getUserId()} is null
	 * Throws {@code AuthorizationException} if there is not @ {@code UserEntity} for {@code authenticationEntity.getUserId()}.
	 * @param authenticationEntity
	 */
	public void validateIdentity(AuthenticationEntity authenticationEntity) {
		if (authenticationEntity == null || authenticationEntity.getUserId() == null) {
			throw new AuthorizationException(AuthorizationException.Type.UNAUTHORIZED);
		}
		UserEntity userEntity = userModel.get(authenticationEntity.getUserId());
		if (userEntity == null) {
			throw new AuthorizationException(AuthorizationException.Type.FORBIDDEN);
		}
	}

}
