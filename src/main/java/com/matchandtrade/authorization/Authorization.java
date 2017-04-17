package com.matchandtrade.authorization;

import com.matchandtrade.persistence.entity.AuthenticationEntity;

public class Authorization {

	/**
	 * Throws {@code AuthorizationException} if {@code authenticationEntity} or {@code authenticationEntity.getUserId()} is null
	 * Throws {@code AuthorizationException} if there is not @ {@code UserEntity} for {@code authenticationEntity.getUserId()}.
	 * @param authenticationEntity
	 */
	public static void validateIdentity(AuthenticationEntity authenticationEntity) {
		if (authenticationEntity == null || authenticationEntity.getUser() == null) {
			throw new AuthorizationException(AuthorizationException.Type.UNAUTHORIZED);
		}
		if (authenticationEntity.getUser() == null) {
			throw new AuthorizationException(AuthorizationException.Type.FORBIDDEN);
		}
	}

}
