package com.matchandtrade.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;

import com.matchandtrade.authentication.UserAuthentication;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.transformer.UserTransformer;
import com.matchandtrade.test.random.UserRandom;

@Component
public class MockFactory {
	
	@Autowired
	private UserModel userModel;
	@Autowired
	private UserTransformer userTransformer;
	
	public UserAuthentication nextRandomUserAuthentication() {
		UserEntity userEntity = userTransformer.transform(UserRandom.next());
		userModel.save(userEntity);
		UserAuthentication result = new UserAuthentication();
		result.setAuthenticated(true);
		result.setEmail(userEntity.getEmail());
		result.setName(userEntity.getName());
		result.setNewUser(true);
		result.setUserId(userEntity.getUserId());
		
		return result;
	}

	public MockHttpServletRequest getHttpRquestWithAuthenticatedUser() {
		UserAuthentication userAuthentication = nextRandomUserAuthentication();
		IntegrationTestStore.add(IntegrationTestStore.StoredObject.UserAuthentication, userAuthentication);
		MockHttpServletRequest result = new MockHttpServletRequest();
		result.getSession().setAttribute("user", userAuthentication);
		return result;
	}

	public MockHttpServletRequest getHttpRquestWithAuthenticatedUser(UserAuthentication userAuthentication) {
		MockHttpServletRequest result = new MockHttpServletRequest();
		result.getSession().setAttribute("user", userAuthentication);
		return result;
	}
	
	public MockHttpServletRequest getHttpRquestWithAuthenticatedUserFromIntegrationTestStore() {
		UserAuthentication userAuthentication = (UserAuthentication) IntegrationTestStore.get(IntegrationTestStore.StoredObject.UserAuthentication);
		return getHttpRquestWithAuthenticatedUser(userAuthentication);
	}
	
}
