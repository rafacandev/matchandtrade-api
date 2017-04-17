package com.matchandtrade.authorization;

import com.matchandtrade.persistence.entity.AuthenticationEntity;

public class AuthorizationValidator {

	/**
	 * Throws {@code AuthorizationException} if {@code authenticationEntity} or {@code authenticationEntity.getUser()} is null
	 * @param authenticationEntity
	 */
	public static void validateIdentity(AuthenticationEntity authenticationEntity) {
		if (authenticationEntity == null || authenticationEntity.getUser() == null) {
			throw new AuthorizationException(AuthorizationException.Type.UNAUTHORIZED);
		}
	}

}
