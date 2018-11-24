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
		givenOffer = new OfferJson();
		givenOffer.setOfferId(-1);
		givenOffer.setOfferedArticleId(-2);
		givenOffer.setWantedArticleId(-3);

		fixture = new OfferValidator();

		when(mockArticleService.find(2)).thenReturn(new ArticleEntity());
		when(mockArticleService.find(3)).thenReturn(new ArticleEntity());
		when(mockArticleService.find(4)).thenReturn(new ArticleEntity());
		fixture.articleService = mockArticleService;

		MembershipEntity membership = new MembershipEntity();
		UserEntity user = new UserEntity();
		user.setUserId(1);
		membership.setUser(user);
		TradeEntity trade = new TradeEntity();
		trade.setTradeId(1);
		membership.setTrade(trade);
		when(mockMembershipService.find(1)).thenReturn(membership);
		fixture.membershipService = mockMembershipService;

		when(mockUserService.findByArticleId(2)).thenReturn(user);
		fixture.userService = mockUserService;

		when(mockTradeService.areArticlesInSameTrade(1, 2, 3)).thenReturn(true);
		fixture.tradeService = mockTradeService;
	}

	@Test(expected = RestException.class)
	public void validatePost_When_OfferOfferedArticleIdIdIsNull_Then_BadRequest() {
		givenOffer.setOfferedArticleId(null);
		try {
			fixture.validatePost(0, givenOffer, 1);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Offer.offeredArticleId is mandatory", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_OfferOfferIdIsNull_Then_BadRequest() {
		givenOffer.setWantedArticleId(null);
		try {
			fixture.validatePost(0, givenOffer, 1);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Offer.wantedArticleId is mandatory", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_OfferOfferedArticleIdAndWantedArticleIdAreEqual_Then_BadRequest() {
		givenOffer.setWantedArticleId(-5);
		givenOffer.setOfferedArticleId(-5);
		try {
			fixture.validatePost(0, givenOffer, 1);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Offer.offeredArticleId and Offer.wantedArticleId must differ", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_OfferOfferedArticleIdIsNotFound_Then_NotFound() {
		try {
			fixture.validatePost(0, givenOffer, 1);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			assertEquals("Offer.offeredArticleId was not found", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_OfferWantedArticleIdIsNotFound_Then_NotFound() {
		givenOffer.setOfferedArticleId(2);
		try {
			fixture.validatePost(0, givenOffer, 1);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			assertEquals("Offer.wantedArticleId was not found", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_AuthenticatedUserDoesNotOwnMembershipId_Then_Forbidden() {
		MembershipEntity membership = new MembershipEntity();
		membership.setUser(new UserEntity());
		when(mockMembershipService.find(1)).thenReturn(membership);
		givenOffer.setOfferedArticleId(2);
		givenOffer.setWantedArticleId(3);
		try {
			fixture.validatePost(1, givenOffer, 1);
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			assertEquals("User.userId does not own Membership.membershipId", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_AuthenticatedUserDoesNotOwnOfferOfferedArticleId_Then_Forbidden() {
		UserEntity user = new UserEntity();
		user.setUserId(2);
		when(mockUserService.findByArticleId(2)).thenReturn(user);
		givenOffer.setOfferedArticleId(2);
		givenOffer.setWantedArticleId(3);
		try {
			fixture.validatePost(1, givenOffer, 1);
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			assertEquals("User.userId does not own Offer.offeredArticleId", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_OfferOfferedArticleIdAndOfferWantedArticleIdAreNotAssociatedToTheSameTradeId_Then_BadRequest() {
		givenOffer.setOfferedArticleId(2);
		givenOffer.setWantedArticleId(4);
		try {
			fixture.validatePost(1, givenOffer, 1);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("Offer.offeredArticleId and Offer.wantedArticleId must be associated to the same Trade.tradeId", e.getDescription());
			throw e;
		}
	}

	public void validatePost_When_OfferOfferedArticleIdAndOfferWantedArticleIdAreAssociatedToTheSameTradeId_Then_Succeeds() {
		givenOffer.setOfferedArticleId(2);
		givenOffer.setWantedArticleId(3);
		fixture.validatePost(1, givenOffer, 1);
	}

}
