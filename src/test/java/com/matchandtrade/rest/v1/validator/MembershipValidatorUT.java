package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.MembershipService;
import com.matchandtrade.rest.service.TradeService;
import com.matchandtrade.rest.service.UserService;
import com.matchandtrade.rest.v1.json.MembershipJson;
import com.matchandtrade.rest.v1.transformer.MembershipTransformer;
import com.matchandtrade.test.helper.SearchHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static com.matchandtrade.persistence.entity.TradeEntity.State.GENERATING_RESULTS;
import static com.matchandtrade.persistence.entity.TradeEntity.State.SUBMITTING_ARTICLES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MembershipValidatorUT {

	private MembershipValidator fixture;
	private UserEntity existingUser;
	private TradeEntity existingTrade;
	private MembershipEntity existingMembershipOwnedByDifferentUser;
	private MembershipJson givenMembership;
	private MembershipTransformer membershipTransformer = new MembershipTransformer();
	@Mock
	private TradeService mockTradeService;
	@Mock
	private UserService mockUserService;
	@Mock
	private MembershipService mockMembershipService;

	@Before
	public void before() {
		fixture = new MembershipValidator();

		existingUser = new UserEntity();
		existingUser.setUserId(1);
		UserEntity existingDifferentUser = new UserEntity();
		existingDifferentUser.setUserId(2);

		existingTrade = new TradeEntity();
		existingTrade.setState(SUBMITTING_ARTICLES);
		existingTrade.setTradeId(11);

		MembershipEntity existingMembership = new MembershipEntity();
		existingMembership.setMembershipId(21);
		existingMembership.setUser(existingUser);
		existingMembership.setTrade(existingTrade);
		givenMembership = membershipTransformer.transform(existingMembership);

		existingMembershipOwnedByDifferentUser = new MembershipEntity();
		existingMembershipOwnedByDifferentUser.setUser(existingDifferentUser);
		existingMembershipOwnedByDifferentUser.setMembershipId(22);

		when(mockUserService.findByUserId(existingUser.getUserId())).thenReturn(existingUser);
		fixture.userService = mockUserService;

		when(mockTradeService.findByTradeId(existingTrade.getTradeId())).thenReturn(existingTrade);
		fixture.tradeService = mockTradeService;

		when(mockMembershipService.findByMembershipId(existingMembershipOwnedByDifferentUser.getMembershipId()))
			.thenReturn(existingMembershipOwnedByDifferentUser);
		when(mockMembershipService.findByTradeIdUserIdType(any(), any(), any(), any(), any()))
			.thenReturn(SearchHelper.buildEmptySearchResult());
		when(mockMembershipService.findByTradeIdUserIdType(eq(givenMembership.getTradeId()), eq(givenMembership.getUserId()), any(), any(), any()))
			.thenReturn(SearchHelper.buildSearchResult(existingMembership));
		fixture.membershipService = mockMembershipService;
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_MembershipIdIsNotFound_Then_NotFound() {
		try {
			fixture.validateDelete(existingUser, 0);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			assertEquals("Membership.membershipId was not found", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_UserDoesNotOwnMembership_Then_Forbidden() {
		try {
			fixture.validateDelete(existingUser, existingMembershipOwnedByDifferentUser.getMembershipId());
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			assertEquals("User.userId does not own Membership.membershipId", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateGet_When_MembershipIsNotFound_Then_NotFound() {
		try {
			fixture.validateGet(0);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			assertEquals("Membership.membershipId was not found", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_MembershipUserIdIsNull_Then_BadRequest() {
		givenMembership.setUserId(null);
		try {
			fixture.validatePost(givenMembership);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Membership.userId is mandatory and must refer to an existing User", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_MembershipUserIdIsNotFound_Then_NotFound() {
		givenMembership.setUserId(0);
		try {
			fixture.validatePost(givenMembership);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			assertEquals("Membership.userId must refer to an existing User", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_MembershipTradeIdIsNull_Then_BadRequest() {
		givenMembership.setTradeId(null);
		try {
			fixture.validatePost(givenMembership);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Membership.tradeId must refer to an existing Trade", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_MembershipTradeIsNotSubmittingArticles_Then_BadRequest() {
		existingTrade.setState(GENERATING_RESULTS);
		try {
			fixture.validatePost(givenMembership);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Trade.State must be SUBMITTING_ARTICLES when creating a new Membership", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_MembershipUserIdAndMemershipTradeIsMustBeUnique_Then_BadRequest() {
		try {
			fixture.validatePost(givenMembership);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Membership.tradeId and Membership.userId combined must be unique", e.getDescription());
			throw e;
		}
	}

}
