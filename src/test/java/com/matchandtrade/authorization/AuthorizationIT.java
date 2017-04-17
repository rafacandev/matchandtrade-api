package com.matchandtrade.authorization;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.repository.UserRepository;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.StringRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AuthorizationIT {

	@Autowired
	private UserRepository userRepository;
	
	@Test(expected=AuthorizationException.class)
	public void validateIdentityNegativeNull() {
		Authorization.validateIdentity(null);
	}
	
	@Test(expected=AuthorizationException.class)
	public void validateIdentityNegativeInvalidUser() {
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setAuthenticationId(-1);
		Authorization.validateIdentity(authenticationEntity);
	}
	
	public void validateIdentityPositive() {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(StringRandom.nextEmail());
		userEntity.setName(StringRandom.nextName());
		userRepository.save(userEntity);
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setUser(userEntity);
		Authorization.validateIdentity(authenticationEntity);
	}
	
}
