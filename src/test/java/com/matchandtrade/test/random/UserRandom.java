package com.matchandtrade.test.random;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.facade.AuthenticationRespositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.rest.v1.transformer.UserTransformer;

@Component
public class UserRandom {
	
	@Autowired
	private UserRepositoryFacade userRepositoryFacade;
	@Autowired
	private AuthenticationRespositoryFacade authenticationRespositoryFacade;
	private UserTransformer userTransformer = new UserTransformer();
	
	public UserEntity createEntity() {
		return userTransformer.transform(createJson());
	}

	public UserJson createJson() {
		UserJson result = new UserJson();
		result.setName(StringRandom.nextName());
		result.setEmail(StringRandom.nextEmail());
		return result;
	}
	
	@Transactional
	public UserEntity createPersistedEntity() {
		UserEntity result = createEntity();
		userRepositoryFacade.save(result);
		return result;
	}

	@Transactional
	public UserEntity createPersistedEntity(String name) {
		UserEntity result = createEntity();
		result.setName(name);
		userRepositoryFacade.save(result);
		return result;
	}

}