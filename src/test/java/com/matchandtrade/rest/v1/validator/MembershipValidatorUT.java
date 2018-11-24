package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.MembershipService;
import com.matchandtrade.rest.service.TradeService;
import com.matchandtrade.rest.service.UserService;
import com.matchandtrade.rest.v1.json.MembershipJson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import static com.matchandtrade.persistence.entity.TradeEntity.State.SUBMITTING_ARTICLES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class MembershipValidatorUT {

	private MembershipValidator fixture;
	@Mock
	private TradeService mockTradeService;
	@Mock
	private UserService mockUserService;
	@Mock
	private MembershipService mockMembershipService;
	private UserEntity givenAuthenticatedUser;
	private MembershipJson givenMembership;
	private TradeEntity expectedTrade;

	@Before
	public void before() {
		fixture = new MembershipValidator();

		givenAuthenticatedUser = new UserEntity();
		givenAuthenticatedUser.setUserId(1);

		givenMembership = new MembershipJson();
		givenMembership.setUserId(1);

		doReturn(givenAuthenticatedUser).when(mockUserService).find(1);
		fixture.userService = mockUserService;

		expectedTrade = new TradeEntity();
		expectedTrade.setState(SUBMITTING_ARTICLES);
		expectedTrade.setTradeId(1);
		doReturn(expectedTrade).when(mockTradeService).find(1);
		fixture.tradeService = mockTradeService;

		fixture.membershipService = mockMembershipService;
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_MembershipIdIsNotFound_Then_NotFound() {
		try {
			fixture.validateDelete(givenAuthenticatedUser, -1);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			assertEquals("Membership.membershipId was not found", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_UserDoesNotOwnMembershipId_Then_Forbidden() {
		MembershipEntity membership = new MembershipEntity();
		membership.setUser(new UserEntity());
		doReturn(membership).when(mockMembershipService).find(1);
		try {
			fixture.validateDelete(givenAuthenticatedUser, 1);
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			assertEquals("User.userId does not own Membership.membershipId", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateGet_When_MembershipIdIsNotFound_Then_NotFound() {
		try {
			fixture.validateGet(-1);
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
			assertEquals("Membership.userId must refer to an existing User", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_MembershipUserIdIsNotFound_Then_BadRequest() {
		givenMembership.setUserId(-1);
		try {
			fixture.validatePost(givenMembership);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Membership.userId must refer to an existing User", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_MembershipTradeIdIsNull_Then_BadRequest() {
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
		givenMembership.setTradeId(1);
		expectedTrade.setState(null);
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
		doReturn(new SearchResult<MembershipEntity>(new ArrayList<>(), new Pagination(1, 1, 1L)))
			.when(mockMembershipService).findByTradeIdUserIdType(1, 1, null, 1, 1);
		givenMembership.setTradeId(1);
		try {
			fixture.validatePost(givenMembership);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Membership.tradeId and Membership.userId combined must be unique", e.getDescription());
			throw e;
		}
	}

}
