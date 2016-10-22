package com.matchandtrade.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.authentication.UserAuthentication;
import com.matchandtrade.controller.RestException;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.UserEntity;


@Component
public class Authorization {

	@Autowired
	private UserModel userModel;
	
	/**
	 * Throws <pre>RestException</pre> if <pre>userAuthentication</pre> or <pre>userAuthentication.userId</pre> is null
	 * @param userAuthentication
	 * @return UserEntity instance of <pre>user.userId</pre>
	 */
	public void doBasicAuthorization(UserAuthentication userAuthentication) {
		if (userAuthentication == null) {
			throw new RestException(HttpStatus.UNAUTHORIZED);
		} else if (userAuthentication.getUserId() == null) {
			throw new RestException(HttpStatus.FORBIDDEN);
		}
	}
	
	/**
	 * Throws <pre>RestException</pre> if user is not <pre>ROLE.ADMINISTRATOR<pre> or
	 * if <pre>userEntity.userId</pre> is not equals to <pre>userId</pre>.
	 * @param userEntity
	 * @param userId
	 */
	public void validateIdentity(UserEntity userEntity, Integer userId) {
		if (userEntity.getRole() != UserEntity.Role.ADMINISTRATOR && !userEntity.getUserId().equals(userId)) {
			throw new RestException(HttpStatus.FORBIDDEN);
		}
	}

	/**
	 * Performs <pre>doBasicAuthorization(userAuthentication)</pre> and <pre>validateIdentity(userEntity, userId)</pre>.
	 * @param userAuthentication
	 * @param userId
	 * @return the result of <pre>doBasicAuthorization(userAuthentication)</pre>
	 */
	public UserEntity validateIdentityAndDoBasicAuthorization(UserAuthentication userAuthentication, Integer userId) {
		doBasicAuthorization(userAuthentication);
		UserEntity userEntity = userModel.get(userAuthentication.getUserId());
		validateIdentity(userEntity, userId);
		return userEntity;
	}
	
}
