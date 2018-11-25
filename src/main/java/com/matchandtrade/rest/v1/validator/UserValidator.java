package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.UserService;
import com.matchandtrade.rest.v1.json.UserJson;

@Component
public class UserValidator {

	@Autowired
	UserService userService;

	/**
	 * <p>{@code RestException(HttpStatus.FORBIDDEN, "User.userId is not reference the authenticated user")}</p>
	 * <p>{@code RestException(HttpStatus.BAD_REQUEST, "User.email cannot be updated")}</p>
	 *
	 * @param authenticatedUser
	 * @param changeRequestUser
	 */
	public void validatePut(UserEntity authenticatedUser, UserJson changeRequestUser) {
		UserEntity targetUser = userService.find(changeRequestUser.getUserId());

		if (targetUser == null) {
			throw new RestException(HttpStatus.NOT_FOUND, "User.userId was not found");
		}

		if (!authenticatedUser.getUserId().equals(targetUser.getUserId())) {
			throw new RestException(HttpStatus.FORBIDDEN, "User.userId is not reference the authenticated user");
		}

		if (changeRequestUser.getEmail() == null || !targetUser.getEmail().equals(changeRequestUser.getEmail())) {
			throw new RestException(HttpStatus.BAD_REQUEST, "User.email cannot be updated");
		}
	}
	
}
