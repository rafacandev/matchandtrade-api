package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.test.helper.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@TestPropertySource(locations = "file:config/matchandtrade.properties")
@SpringBootTest
public class TradeServiceIT {

	@Autowired
	private ArticleHelper articleHelper;
	@Autowired
	private ListingHelper listingHelper;
	@Autowired
	private MembershipHelper membershipHelper;
	@Autowired
	private UserHelper userHelper;
	private TradeEntity existingTrade;
	@Autowired
	private TradeHelper tradeHelper;
	@Autowired
	private TradeService fixture;

	@Before
	public void before() {
		existingTrade = tradeHelper.createPersistedEntity(userHelper.createPersistedEntity());
	}

	@Test
	public void areArticlesInSameTrade_When_MembershipDoesNotExist_Then_False() {
		boolean actual = fixture.areArticlesInSameTrade(-1, 1, 2);
		assertFalse(actual);
	}

	@Test
	public void areArticlesInSameTrade_When_TradeExistsButArticleDoesNot_Then_False() {
		boolean actual = fixture.areArticlesInSameTrade(existingTrade.getTradeId(), -1);
		assertFalse(actual);
	}

	@Test
	public void areArticlesInSameTrade_When_1ArticleInTrade_Then_True() {
		MembershipEntity firstMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity(), existingTrade);
		ArticleEntity firstArticle = articleHelper.createPersistedEntity(firstMembership);
		boolean actual = fixture.areArticlesInSameTrade(existingTrade.getTradeId(), firstArticle.getArticleId());
		assertTrue(actual);
	}

	@Test
	public void areArticlesInSameTrade_When_1ArticleIsInTradeBut1ArticleIsNotInTrade_Then_False() {
		MembershipEntity firstMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity(), existingTrade);
		ArticleEntity firstArticle = articleHelper.createPersistedEntity(firstMembership);
		ArticleEntity secondArticle = articleHelper.createPersistedEntity();
		boolean actual = fixture.areArticlesInSameTrade(existingTrade.getTradeId(), firstArticle.getArticleId(), secondArticle.getArticleId());
		assertFalse(actual);
	}

	@Test
	public void areArticlesInSameTrade_When_2ArticlesAreInTrade_Then_True() {
		MembershipEntity firstMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity(), existingTrade);
		ArticleEntity firstArticle = articleHelper.createPersistedEntity(firstMembership);
		MembershipEntity secondMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity(), existingTrade);
		ArticleEntity secondArticle = articleHelper.createPersistedEntity(secondMembership);
		boolean actual = fixture.areArticlesInSameTrade(existingTrade.getTradeId(), firstArticle.getArticleId(), secondArticle.getArticleId());
		assertTrue(actual);
	}

	@Test
	public void areArticlesInSameTrade_When_2ArticlesAreInSameTradepBut1ArticleIsNotInTrade_Then_False() {
		MembershipEntity firstMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity(), existingTrade);
		ArticleEntity firstArticle = articleHelper.createPersistedEntity(firstMembership);
		MembershipEntity secondMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity(), existingTrade);
		ArticleEntity secondArticle = articleHelper.createPersistedEntity(secondMembership);
		MembershipEntity thirdMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity(), tradeHelper.createPersistedEntity());
		ArticleEntity thirdArticle = articleHelper.createPersistedEntity(thirdMembership);
		boolean actual = fixture.areArticlesInSameTrade(existingTrade.getTradeId(), firstArticle.getArticleId(), secondArticle.getArticleId(), thirdArticle.getArticleId());
		assertFalse(actual);
	}

}
