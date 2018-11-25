package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.UserService;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.rest.v1.transformer.UserTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserValidatorUT {
	private UserEntity existingAuthenticatedUser;
	private UserJson givenUser;
	private UserValidator fixture;
	@Mock
	private UserService mockUserService;
	private UserTransformer userTransformer = new UserTransformer();

	@Before
	public void before() {
		fixture = new UserValidator();
		existingAuthenticatedUser = new UserEntity();
		existingAuthenticatedUser.setUserId(1);
		existingAuthenticatedUser.setEmail("existing-authenticated-user@test.com");
		givenUser = userTransformer.transform(existingAuthenticatedUser);
		UserEntity existingUserDifferent = new UserEntity();
		existingUserDifferent.setUserId(2);
		existingUserDifferent.setEmail("existing-unrelated-user@test.com");

		when(mockUserService.find(existingAuthenticatedUser.getUserId())).thenReturn(existingAuthenticatedUser);
		when(mockUserService.find(existingUserDifferent.getUserId())).thenReturn(existingUserDifferent);
		fixture.userService = mockUserService;
	}

	@Test(expected = RestException.class)
	public void validatePut_When_AuthenticatedUserIsNotSameAsGivenUser_Then_Forbidden() {
		givenUser.setUserId(999);
		try {
			fixture.validatePut(existingAuthenticatedUser, givenUser);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			assertEquals("User.userId was not found", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePut_When_AuthenticatedUserIsSameAsGiveUserButEmailIsDifferent_Then_BadRequest() {
		givenUser.setEmail("different-email@test.com");
		try {
			fixture.validatePut(existingAuthenticatedUser, givenUser);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("User.email cannot be updated", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePut_When_AuthenticatedUserIsSameAsGiveUserButEmailIsNull_Then_Forbidden() {
		givenUser.setEmail(null);
		try {
			fixture.validatePut(existingAuthenticatedUser, givenUser);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("User.email cannot be updated", e.getDescription());
			throw e;
		}
	}

	@Test
	public void validatePut_When_AuthenticatedUserIsSameAsGivenUserAndEmailDidNotChange_Then_Succeeds() {
		fixture.validatePut(existingAuthenticatedUser, givenUser);
	}
}