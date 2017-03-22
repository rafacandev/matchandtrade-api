package com.matchandtrade.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.authentication.AuthenticationResponseJson;
import com.matchandtrade.model.AuthenticationModel;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.transformer.UserTransformer;
import com.matchandtrade.test.random.StringRandom;
import com.matchandtrade.test.random.UserRandom;

@Component
public class MockFactory {
	
	@Autowired
	private AuthenticationModel authenticationModel;
	@Autowired
	private UserModel userModel;
	@Autowired
	private UserTransformer userTransformer;
	
	public AuthenticationResponseJson nextRandomUserAuthenticationPersisted(String antiForgeryState) {
		UserEntity userEntity = userTransformer.transform(UserRandom.nextJson());
		userModel.save(userEntity);

		AuthenticationResponseJson result = new AuthenticationResponseJson(
				userEntity.getUserId(), 
				true, 
				userEntity.getEmail(), 
				userEntity.getName(), 
				StringRandom.nextString());
		
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setUserId(userEntity.getUserId());
		authenticationEntity.setToken(userEntity.getUserId().toString());
		authenticationEntity.setAntiForgeryState(antiForgeryState);
		authenticationModel.save(authenticationEntity);
		return result;
	}

}
