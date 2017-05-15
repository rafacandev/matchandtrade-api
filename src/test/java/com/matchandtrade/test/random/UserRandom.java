package com.matchandtrade.test.random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.repository.UserRepository;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.rest.v1.transformer.UserTransformer;

@Component
public class UserRandom {
	
	@Autowired
	private UserRepository userRepository;
	
	public static UserEntity nextEntity() {
		return UserTransformer.transform(nextJson());
	}

	public static UserJson nextJson() {
		UserJson result = new UserJson();
		result.setName(StringRandom.nextName());
		result.setEmail(StringRandom.nextEmail());
		return result;
	}
	
	@Transactional
	public UserEntity nextPersistedEntity() {
		UserEntity result = nextEntity();
		userRepository.save(result);
		return result;
	}
}