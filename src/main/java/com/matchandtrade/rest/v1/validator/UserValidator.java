package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.UserJson;

@Component
public class UserValidator {

	@Autowired
	private UserModel userModel;

	/**
	 * <i>UserJson.email</i> cannot change on PUT operations.
	 * @param userId
	 * @param json
	 */
	public void validatePut(UserJson json) {
		UserEntity userEntity = userModel.get(json.getUserId());
		if (!userEntity.getEmail().equals(json.getEmail())) {
			throw new ValidationException(ValidationException.ErrorType.INVALID_OPERATION, "Cannot update User.email on PUT operations.");
		}
	}
	
	// TODO validatePost. email uniqueness

}
