package com.matchandtrade.authorization;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.StringRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AuthorizationIT {

	@Autowired
	private Authorization authorization;
	@Autowired
	private UserModel userModel;
	
	@Test(expected=AuthorizationException.class)
	public void validateIdentityNegativeNull() {
		authorization.validateIdentity(null);
	}
	
	@Test(expected=AuthorizationException.class)
	public void validateIdentityNegativeInvalidUser() {
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setAuthenticationId(-1);
		authenticationEntity.setUserId(-1);
		authorization.validateIdentity(authenticationEntity);
	}
	
	public void validateIdentityPositive() {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(StringRandom.nextEmail());
		userEntity.setName(StringRandom.nextName());
		userModel.save(userEntity);
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setUserId(userEntity.getUserId());
		authorization.validateIdentity(authenticationEntity);
	}
	
}
