package com.matchandtrade.authorization;

import org.springframework.http.HttpStatus;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.rest.RestException;

public class AuthorizationValidator {

	// Utility classes should not have public constructors
	private AuthorizationValidator() {}
	
	/**
	 * Throws {@code AuthorizationException} if {@code authenticationEntity} or {@code authenticationEntity.getUser()} is null
	 * @param authenticationEntity
	 */
	public static void validateIdentity(AuthenticationEntity authenticationEntity) {
		if (authenticationEntity == null || authenticationEntity.getUser() == null) {
			throw new RestException(HttpStatus.UNAUTHORIZED);
		}
	}

}
