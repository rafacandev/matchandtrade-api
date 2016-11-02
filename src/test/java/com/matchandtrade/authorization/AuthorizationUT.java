package com.matchandtrade.authorization;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.authentication.UserAuthentication;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.test.MockFactory;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AuthorizationUT {

	@Autowired
	private Authorization authorization;
	@Autowired
	private MockFactory mockFactory;
	@Autowired
	private UserModel userModel;
	
	@Test(expected=AuthorizationException.class)
	public void doBasicAuthorizationNegativeNull() {
		authorization.doBasicAuthorization(null);
	}
	
	@Test(expected=AuthorizationException.class)
	public void doBasicAuthorizationNegativeUserIdNull() {
		authorization.doBasicAuthorization(new UserAuthentication());
	}

	@Test(expected=AuthorizationException.class)
	public void doBasicAuthorizationNegative() {
		authorization.validateIdentity(null, null);
	}

	@Test(expected=AuthorizationException.class)
	public void validateIdentityAndDoBasicAuthorizationNegativeUserDoesNotExist() {
		UserAuthentication userAuthentication = new UserAuthentication();
		userAuthentication.setUserId(-1);
		authorization.validateIdentityAndDoBasicAuthorization(userAuthentication, -1);
	}

	@Test(expected=AuthorizationException.class)
	public void validateIdentityAndDoBasicAuthorizationNegativeUserIdMismatch() {
		UserAuthentication userAuthentication = mockFactory.nextRandomUserAuthenticationPersisted();
		authorization.validateIdentityAndDoBasicAuthorization(userAuthentication, -1);
	}
	
	@Test(expected=AuthorizationException.class)
	public void validateIdentityAndDoBasicAuthorizationNegativeUserNull() {
		UserAuthentication userAuthentication = mockFactory.nextRandomUserAuthenticationPersisted();
		authorization.validateIdentityAndDoBasicAuthorization(userAuthentication, null);
	}

	@Test
	@Rollback(false)
	public void validateIdentityAndDoBasicAuthorizationPositive() {
		UserAuthentication userAuthentication = mockFactory.nextRandomUserAuthenticationPersisted();
		UserEntity result = authorization.validateIdentityAndDoBasicAuthorization(userAuthentication, userAuthentication.getUserId());
		Assert.assertEquals(userAuthentication.getUserId(), result.getUserId());
	}

	@Test
	@Rollback(false)
	public void validateIdentityAndDoBasicAuthorizationPositiveAdminstrator() {
		UserAuthentication userAuthentication = mockFactory.nextRandomUserAuthenticationPersisted();
		UserEntity userEntity = userModel.get(userAuthentication.getUserId());
		userEntity.setRole(UserEntity.Role.ADMINISTRATOR);
		userModel.save(userEntity);
		UserAuthentication anotherUser = mockFactory.nextRandomUserAuthenticationPersisted();
		UserEntity result = authorization.validateIdentityAndDoBasicAuthorization(userAuthentication, anotherUser.getUserId());
		Assert.assertEquals(userAuthentication.getUserId(), result.getUserId());
	}
	
}
