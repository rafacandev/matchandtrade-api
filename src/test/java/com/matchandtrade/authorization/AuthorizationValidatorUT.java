package com.matchandtrade.authorization;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import org.junit.Test;

public class AuthorizationValidatorUT {

	@Test(expected=RestException.class)
	public void validateIdentity_When_AuthenticationEntityIsNull_Then_RestException() {
		AuthorizationValidator.validateIdentity(null);
	}
	
	@Test(expected=RestException.class)
	public void validateIdentity_When_AuthenticationEntityUserIsNull_Then_RestException() {
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		AuthorizationValidator.validateIdentity(authenticationEntity);
	}

	@Test
	public void validateIdentity_When_AuthenticationEntityUserIsNotNull_Then_RestException() {
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setUser(new UserEntity());
		AuthorizationValidator.validateIdentity(authenticationEntity);
	}

}
