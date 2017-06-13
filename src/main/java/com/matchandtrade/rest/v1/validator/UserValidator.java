package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.UserService;
import com.matchandtrade.rest.v1.json.UserJson;

@Component
public class UserValidator {

	@Autowired
	private UserService userService;

	/**
	 * {@code UserJson.email} cannot change on PUT operations.
	 * @param json
	 */
	public void validatePut(UserJson json) {
		UserEntity userEntity = userService.get(json.getUserId());
		if (json.getEmail() == null || userEntity == null || !json.getEmail().equalsIgnoreCase(userEntity.getEmail())) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Cannot change User.email on PUT operations.");
		}
	}
	
	public void validateGetById(AuthenticationEntity authenticationEntity, Integer userId) {
		if (!authenticationEntity.getUser().getUserId().equals(userId)) {
			throw new RestException(HttpStatus.FORBIDDEN, "No permissions to get the User for this userId.");
		}
	}
}
