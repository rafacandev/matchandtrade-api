package com.matchandtrade.test.helper;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.AuthenticationRespositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.test.random.StringRandom;
import com.matchandtrade.test.random.UserRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ControllerHelper {
	@Autowired
	private AuthenticationRespositoryFacade authenticationRespositoryFacade;
	@Autowired
	private UserRepositoryFacade userRepositoryFacade;
	@Autowired
	private UserRandom userRandom;

	public String generateAuthorizationHeader() {
		String authorizationToken = StringRandom.nextString();
		saveUserWithAuthorizationToken(userRandom.createEntity(), authorizationToken);
		return authorizationToken;
	}

	public String generateAuthorizationHeader(UserEntity userEntity) {
		String authorizationToken = StringRandom.nextString();
		saveUserWithAuthorizationToken(userEntity, authorizationToken);
		return authorizationToken;
	}

	@Transactional
	private UserEntity saveUserWithAuthorizationToken(UserEntity result, String authorizationToken) {
		userRepositoryFacade.save(result);
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setAntiForgeryState(StringRandom.nextString());
		authenticationEntity.setToken(authorizationToken);
		authenticationEntity.setUser(result);
		authenticationRespositoryFacade.save(authenticationEntity);
		return result;
	}

}
