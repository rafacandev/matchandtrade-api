package com.matchandtrade.test.helper;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.AuthenticationRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.test.StringRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Commit
public class ControllerHelper {
	@Autowired
	private AuthenticationRepositoryFacade authenticationRepositoryFacade;
	@Autowired
	private UserRepositoryFacade userRepositoryFacade;

	public String generateAuthorizationHeader(UserEntity userEntity) {
		String authorizationToken = StringRandom.nextString();
		userRepositoryFacade.save(userEntity);
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setAntiForgeryState(StringRandom.nextString());
		authenticationEntity.setToken(authorizationToken);
		authenticationEntity.setUser(userEntity);
		authenticationRepositoryFacade.save(authenticationEntity);
		return authorizationToken;
	}

}
