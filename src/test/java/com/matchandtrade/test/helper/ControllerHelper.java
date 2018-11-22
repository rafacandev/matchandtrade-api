package com.matchandtrade.test.helper;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.AuthenticationRespositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.test.StringRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControllerHelper {
	@Autowired
	private AuthenticationRespositoryFacade authenticationRespositoryFacade;
	@Autowired
	private UserRepositoryFacade userRepositoryFacade;

	public String generateAuthorizationHeader(UserEntity userEntity) {
		String authorizationToken = StringRandom.nextString();
		userRepositoryFacade.save(userEntity);
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setAntiForgeryState(StringRandom.nextString());
		authenticationEntity.setToken(authorizationToken);
		authenticationEntity.setUser(userEntity);
		authenticationRespositoryFacade.save(authenticationEntity);
		return authorizationToken;
	}

}
