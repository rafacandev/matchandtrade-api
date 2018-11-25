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
import com.matchandtrade.rest.v1.transformer.TradeTransformer;
import com.matchandtrade.test.StringRandom;
import com.matchandtrade.test.helper.SearchHelper;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TradeValidatorUT {
	private static final String EXPECTED_DUPLICATE_NAME = "duplicate-name";

	private TradeValidator fixture = new TradeValidator();
	private TradeJson givenTrade;
	private TradeEntity existingTrade;
	private TradeEntity existingTradeOwnedByDifferentUser;
	private UserEntity existingUser;
	@Mock
	private MembershipService mockMembershipService;
	@Mock
	private TradeService mockTradeService;
	private TradeTransformer tradeTransformer = new TradeTransformer();

	@Before
	public void before() {
		String expectedUniqueName = "unique-name";
		existingUser = new UserEntity();
		existingUser.setUserId(1);
		UserEntity existingUserDifferent = new UserEntity();
		existingUserDifferent.setUserId(2);

		givenTrade = new TradeJson();
		givenTrade.setTradeId(11);
		givenTrade.setName(expectedUniqueName);
		existingTrade = tradeTransformer.transform(givenTrade);
		existingTradeOwnedByDifferentUser = new TradeEntity();
		existingTradeOwnedByDifferentUser.setTradeId(12);

		when(mockTradeService.isNameUnique(expectedUniqueName)).thenReturn(true);
		when(mockTradeService.isNameUnique("abc")).thenReturn(true);
		when(mockTradeService.isNameUnique(EXPECTED_DUPLICATE_NAME)).thenReturn(false);
		when(mockTradeService.isNameUniqueExceptForTradeId(expectedUniqueName, givenTrade.getTradeId())).thenReturn(true);
		when(mockTradeService.find(givenTrade.getTradeId())).thenReturn(existingTrade);
		when(mockTradeService.find(existingTradeOwnedByDifferentUser.getTradeId())).thenReturn(existingTradeOwnedByDifferentUser);
		fixture.tradeService = mockTradeService;

		when(mockMembershipService.findByTradeIdUserIdType(existingTrade.getTradeId(), existingUser.getUserId(), OWNER, 1, 1))
			.thenReturn(SearchHelper.buildSearchResult(new MembershipEntity()));
		when(mockMembershipService.findByTradeIdUserIdType(existingTrade.getTradeId(), existingUserDifferent.getUserId(), OWNER, 1, 1))
			.thenReturn(SearchHelper.buildEmptySearchResult());
		when(mockMembershipService.findByTradeIdUserIdType(existingTradeOwnedByDifferentUser.getTradeId(), existingUser.getUserId(), OWNER, 1, 1))
			.thenReturn(SearchHelper.buildEmptySearchResult());
		fixture.membershipService = mockMembershipService;
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_TradeDoesNotExist_Then_NotFound() {
		try {
			fixture.validateDelete(existingUser, 0);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			assertEquals("Trade.tradeId was not found", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_UserDoesNotOwnTrade_Then_Succeeds() {
		try {
			fixture.validateDelete(existingUser, existingTradeOwnedByDifferentUser.getTradeId());
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			assertEquals("User.userId: 1 is not the owner of Trade.tradeId: 12", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateGet_When_TradeDoesNotExist_Then_NotFound() {
		try {
			fixture.validateGet(0);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			assertEquals("Trade.tradeId was not found", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameAlreadyExists_Then_BadRequest() {
		givenTrade.setName(EXPECTED_DUPLICATE_NAME);
		try {
			fixture.validatePost(givenTrade);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Trade.name must be unique", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_Description1001Characters_Then_BadRequest() {
		givenTrade.setDescription(StringRandom.sequentialNumericString(1001));
		try {
			fixture.validatePost(givenTrade);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Trade.description must be between 3 and 1000 in length", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_DescriptionHas2Characters_Then_BadRequest() {
		givenTrade.setDescription("ab");
		try {
			fixture.validatePost(givenTrade);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Trade.description must be between 3 and 1000 in length", e.getDescription());
			throw e;
		}
	}

	@Test
	public void validatePost_When_DescriptionIsNull_Then_Succeeds() {
		givenTrade.setDescription(null);
		fixture.validatePost(givenTrade);
	}

	@Test
	public void validatePost_When_DescriptionHas3Characters_Then_Succeeds() {
		givenTrade.setDescription("abc");
		fixture.validatePost(givenTrade);
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameHas150Characters_Then_BadRequest() {
		givenTrade.setName(StringRandom.sequentialNumericString(151));
		try {
			fixture.validatePost(givenTrade);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameIsNull_Then_BadRequest() {
		try {
			fixture.validatePost(new TradeJson());
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameHas2Characters_Then_BadRequest() {
		givenTrade.setName("ab");
		try {
			fixture.validatePost(givenTrade);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void validatePost_When_NameHas3Characters_Then_Succeeds() {
		givenTrade.setName("abc");
		fixture.validatePost(givenTrade);
	}

	@Test(expected = RestException.class)
	public void validatePut_When_UserDoesNotOwnTrade_Then_Forbidden() {
		givenTrade.setTradeId(existingTradeOwnedByDifferentUser.getTradeId());
		try {
			fixture.validatePut(givenTrade, existingUser);
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePut_When_NameAlreadyExists_Then_BadRequest() {
		givenTrade.setName(EXPECTED_DUPLICATE_NAME);
		try {
			fixture.validatePut(givenTrade, existingUser);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void validatePut_When_NameIsUnique_Then_Succeeds() {
		fixture.validatePut(givenTrade, existingUser);
	}

}
