package com.matchandtrade.test.helper;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.rest.v1.transformer.UserTransformer;
import com.matchandtrade.test.StringRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Commit
public class UserHelper {
	
	@Autowired
	private UserRepositoryFacade userRepositoryFacade;
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
	
	public UserEntity createPersistedEntity() {
		UserEntity result = createEntity();
		userRepositoryFacade.save(result);
		return result;
	}

	public UserEntity createPersistedEntity(String name) {
		UserEntity result = createEntity();
		result.setName(name);
		userRepositoryFacade.save(result);
		return result;
	}

}