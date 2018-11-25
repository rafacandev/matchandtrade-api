package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.service.MembershipService;
import com.matchandtrade.rest.service.TradeService;
import com.matchandtrade.rest.service.UserService;
import com.matchandtrade.rest.v1.json.OfferJson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OfferValidatorUT {

	private OfferJson givenOffer;
	private OfferValidator fixture;
	private ArticleEntity existingArticleOwnedByDifferentUser;
	private ArticleEntity existingArticle;
	private ArticleEntity existingArticleNotInTrade;
	private MembershipEntity existingMembership;
	private MembershipEntity existingMembershipOwnedByDifferentUser;
	private UserEntity existingUser;
	private UserEntity existingUserDifferent;

	@Mock
	private ArticleService mockArticleService;
	@Mock
	private MembershipService mockMembershipService;
	@Mock
	private UserService mockUserService;
	@Mock
	private TradeService mockTradeService;

	@Before
	public void before() {
		fixture = new OfferValidator();
		existingUser = new UserEntity();
		existingUser.setUserId(1);
		existingUserDifferent = new UserEntity();
		existingUserDifferent.setUserId(2);

		TradeEntity existingTrade = new TradeEntity();
		existingTrade.setTradeId(11);
		existingMembership = new MembershipEntity();
		existingMembership.setMembershipId(21);
		existingMembership.setUser(existingUser);
		existingMembership.setTrade(existingTrade);
		existingMembershipOwnedByDifferentUser = new MembershipEntity();
		existingMembershipOwnedByDifferentUser.setMembershipId(22);
		existingMembershipOwnedByDifferentUser.setUser(existingUserDifferent);

		existingArticle = new ArticleEntity();
		existingArticle.setArticleId(31);
		existingArticleOwnedByDifferentUser = new ArticleEntity();
		existingArticleOwnedByDifferentUser.setArticleId(32);
		existingArticleNotInTrade = new ArticleEntity();
		existingArticleNotInTrade.setArticleId(33);

		givenOffer = new OfferJson();
		givenOffer.setOfferId(41);
		givenOffer.setOfferedArticleId(existingArticle.getArticleId());
		givenOffer.setWantedArticleId(existingArticleOwnedByDifferentUser.getArticleId());

		when(mockArticleService.find(existingArticle.getArticleId())).thenReturn(existingArticle);
		when(mockArticleService.find(existingArticleNotInTrade.getArticleId())).thenReturn(existingArticleNotInTrade);
		when(mockArticleService.find(existingArticleOwnedByDifferentUser.getArticleId())).thenReturn(existingArticleOwnedByDifferentUser);
		fixture.articleService = mockArticleService;

		when(mockMembershipService.find(existingMembership.getMembershipId())).thenReturn(existingMembership);
		when(mockMembershipService.find(existingMembershipOwnedByDifferentUser.getMembershipId())).thenReturn(existingMembershipOwnedByDifferentUser);
		fixture.membershipService = mockMembershipService;

		when(mockUserService.findByArticleId(existingArticle.getArticleId())).thenReturn(existingUser);
		when(mockUserService.findByArticleId(existingArticleOwnedByDifferentUser.getArticleId())).thenReturn(existingUserDifferent);
		when(mockUserService.findByOfferId(givenOffer.getOfferId())).thenReturn(existingUser);
		fixture.userService = mockUserService;

		when(mockTradeService.areArticlesInSameTrade(
				existingTrade.getTradeId(), givenOffer.getOfferedArticleId(), givenOffer.getWantedArticleId()))
			.thenReturn(true);
		fixture.tradeService = mockTradeService;
	}

	@Test
	public void validateDelete_When_UserOwnsOffer_Then_Succeeds() {
		fixture.validateDelete(existingMembership.getMembershipId(), givenOffer.getOfferId(), existingUser.getUserId());
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_UserDoesNotOwnMembership_Then_Forbidden() {
		try {
			fixture.validateDelete(existingMembership.getMembershipId(), givenOffer.getOfferId(), existingUserDifferent.getUserId());
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			assertEquals("User.userId does not own Membership.membershipId", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_UserDoesNotOwnOffer_Then_Forbidden() {
		try {
			fixture.validateDelete(existingMembershipOwnedByDifferentUser.getMembershipId(), givenOffer.getOfferId(), existingUserDifferent.getUserId());
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			assertEquals("User.userId does not own Offer.offerId", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_OfferedArticleIdIdIsNull_Then_BadRequest() {
		givenOffer.setOfferedArticleId(null);
		try {
			fixture.validatePost(existingMembership.getMembershipId(), givenOffer, existingUser.getUserId());
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Offer.offeredArticleId is mandatory", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_WantedArticleIdIdIsNull_Then_BadRequest() {
		givenOffer.setWantedArticleId(null);
		try {
			fixture.validatePost(existingMembership.getMembershipId(), givenOffer, existingUser.getUserId());
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Offer.wantedArticleId is mandatory", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_OfferedArticleAndWantedArticleAreEqual_Then_BadRequest() {
		givenOffer.setOfferedArticleId(existingArticle.getArticleId());
		givenOffer.setWantedArticleId(existingArticle.getArticleId());
		try {
			fixture.validatePost(existingMembership.getMembershipId(), givenOffer, existingUser.getUserId());
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Offer.offeredArticleId and Offer.wantedArticleId must differ", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_OfferedArticleIsNotFound_Then_NotFound() {
		givenOffer.setOfferedArticleId(0);
		try {
			fixture.validatePost(existingMembership.getMembershipId(), givenOffer, existingUser.getUserId());
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			assertEquals("Offer.offeredArticleId was not found", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_WantedArticleIsNotFound_Then_NotFound() {
		givenOffer.setWantedArticleId(0);
		try {
			fixture.validatePost(existingMembership.getMembershipId(), givenOffer, existingUser.getUserId());
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			assertEquals("Offer.wantedArticleId was not found", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_UserDoesNotOwnMembership_Then_Forbidden() {
		try {
			fixture.validatePost(existingMembershipOwnedByDifferentUser.getMembershipId(), givenOffer, existingUser.getUserId());
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			assertEquals("User.userId does not own Membership.membershipId", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_UserDoesNotOwnOfferedArticleId_Then_Forbidden() {
		givenOffer.setOfferedArticleId(existingArticleOwnedByDifferentUser.getArticleId());
		givenOffer.setWantedArticleId(existingArticle.getArticleId());
		try {
			fixture.validatePost(existingMembership.getMembershipId(), givenOffer, existingUser.getUserId());
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			assertEquals("User.userId does not own Offer.offeredArticleId", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_OfferedArticleAndWantedArticleAreNotAssociatedToTheSameTrade_Then_BadRequest() {
		givenOffer.setOfferedArticleId(existingArticle.getArticleId());
		givenOffer.setWantedArticleId(existingArticleNotInTrade.getArticleId());
		try {
			fixture.validatePost(existingMembership.getMembershipId(), givenOffer, existingUser.getUserId());
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Offer.offeredArticleId and Offer.wantedArticleId must be associated to the same Trade.tradeId", e.getDescription());
			throw e;
		}
	}

	public void validatePost_When_OfferedArticleAndWantedArticleAreAssociatedToTheSameTrade_Then_Succeeds() {
		fixture.validatePost(existingMembership.getMembershipId(), givenOffer, existingUser.getUserId());
	}

}
