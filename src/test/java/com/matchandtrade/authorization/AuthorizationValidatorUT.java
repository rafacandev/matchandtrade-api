package com.matchandtrade.authorization;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AuthorizationValidatorIT {

	@Test(expected=RestException.class)
	public void validateIdentityNegativeNull() {
		AuthorizationValidator.validateIdentity(null);
	}
	
	@Test(expected=RestException.class)
	public void validateIdentityNegativeInvalidUser() {
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		AuthorizationValidator.validateIdentity(authenticationEntity);
	}
	
}
