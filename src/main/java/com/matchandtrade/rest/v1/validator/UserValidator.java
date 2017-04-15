package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.repository.UserRespository;
import com.matchandtrade.rest.v1.json.UserJson;

@Component
public class UserValidator {

	@Autowired
	private UserRespository userRepository;

	/**
	 * <i>UserJson.email</i> cannot change on PUT operations.
	 * @param userId
	 * @param json
	 */
	public void validatePut(UserJson json) {
		UserEntity userEntity = userRepository.get(json.getUserId());
		if (json.getEmail() == null || userEntity == null || !json.getEmail().equals(userEntity.getEmail())) {
			throw new ValidationException(ValidationException.ErrorType.INVALID_OPERATION, "Cannot update User.email on PUT operations.");
		}
	}
	
	// TODO validatePost. email uniqueness
	
	
	public void validateGetById(AuthenticationEntity authenticationEntity, Integer userId) {
		if (!authenticationEntity.getUserId().equals(userId)) {
			throw new AuthorizationException(AuthorizationException.Type.UNAUTHORIZED);
		}
	}
}
