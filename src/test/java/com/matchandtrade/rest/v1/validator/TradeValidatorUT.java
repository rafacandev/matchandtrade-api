package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.MembershipService;
import com.matchandtrade.rest.service.TradeService;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.test.StringRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static com.matchandtrade.persistence.entity.MembershipEntity.Type.OWNER;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class TradeValidatorUT {

	private TradeValidator fixture = new TradeValidator();
	private final String expectedDuplicatedName = "duplicated-name";
	private TradeJson expectedTrade;
	private UserEntity expectedAuthenticatedUser;
	@Mock
	private MembershipService mockMembershipService;
	@Mock
	private TradeService mockTradeService;

	@Before
	public void before() {
		String expectedUniqueName = "unique-name";
		expectedAuthenticatedUser = new UserEntity();
		expectedAuthenticatedUser.setUserId(1);
		expectedTrade = new TradeJson();
		expectedTrade.setTradeId(1);
		expectedTrade.setName(expectedUniqueName);

		doReturn(true).when(mockTradeService).isNameUnique(expectedUniqueName);
		doReturn(false).when(mockTradeService).isNameUnique(expectedDuplicatedName);
		doReturn(new TradeEntity()).when(mockTradeService).find(1);
		doReturn(true).when(mockTradeService).isNameUniqueExceptForTradeId(expectedUniqueName, 1);
		fixture.tradeService = mockTradeService;

		fixture.membershipService = mockMembershipService;
	}

	private void mockMembershipServiceFindByTradeIdUserIdTypeWithPaginationTotal(long paginationTotal) {
		SearchResult<MembershipEntity> searchResult = new SearchResult<>(null, new Pagination(1, 1, paginationTotal));
		doReturn(searchResult).when(mockMembershipService).findByTradeIdUserIdType(eq(1), eq(1), eq(OWNER), eq(1), eq(1));
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_TradeDoesNotExist_Then_NotFound() {
		try {
			fixture.validateDelete(expectedAuthenticatedUser, 0);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_UserDoesNotOwnTrade_Then_Succeeds() {
		mockMembershipServiceFindByTradeIdUserIdTypeWithPaginationTotal(0L);
		try {
			fixture.validateDelete(expectedAuthenticatedUser, 1);
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateGet_When_TradeDoesNotExist_Then_NotFound() {
		try {
			fixture.validateGet(0);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameAlreadyExists_Then_BadRequest() {
		expectedTrade.setName(expectedDuplicatedName);
		try {
			fixture.validatePost(expectedTrade);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_Description1001Characters_Then_BadRequest() {
		expectedTrade.setDescription(StringRandom.sequentialNumericString(1001));
		try {
			fixture.validatePost(expectedTrade);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_DescriptionHas2Characters_Then_BadRequest() {
		expectedTrade.setDescription("ab");
		try {
			fixture.validatePost(expectedTrade);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void validatePost_When_DescriptionIsNull_Then_Succeeds() {
		expectedTrade.setDescription(null);
		fixture.validatePost(expectedTrade);
	}

	@Test
	public void validatePost_When_DescriptionHas3Characters_Then_Succeeds() {
		expectedTrade.setDescription("abc");
		fixture.validatePost(expectedTrade);
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameHas150Characters_Then_BadRequest() {
		TradeJson json = new TradeJson();
		json.setName(StringRandom.sequentialNumericString(151));
		try {
			fixture.validatePost(json);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameIsNull_Then_BadRequest() {
		TradeJson json = new TradeJson();
		try {
			fixture.validatePost(json);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameHas2Characters_Then_BadRequest() {
		expectedTrade.setName("ab");
		try {
			fixture.validatePost(expectedTrade);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void validatePost_When_NameHas3Characters_Then_Succeeds() {
		doReturn(true).when(mockTradeService).isNameUnique("abc");
		expectedTrade.setName("abc");
		fixture.validatePost(expectedTrade);
	}

	@Test(expected = RestException.class)
	public void validatePut_When_UserDoesNotOwnTrade_Then_Forbidden() {
		mockMembershipServiceFindByTradeIdUserIdTypeWithPaginationTotal(0L);
		try {
			fixture.validatePut(expectedTrade, expectedAuthenticatedUser);
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePut_When_NameAlreadyExists_Then_BadRequest() {
		mockMembershipServiceFindByTradeIdUserIdTypeWithPaginationTotal(1L);
		expectedTrade.setName(expectedDuplicatedName);
		try {
			fixture.validatePut(expectedTrade, expectedAuthenticatedUser);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void validatePut_When_NameIsUnique_Then_Succeeds() {
		mockMembershipServiceFindByTradeIdUserIdTypeWithPaginationTotal(1L);
		fixture.validatePut(expectedTrade, expectedAuthenticatedUser);
	}

}
