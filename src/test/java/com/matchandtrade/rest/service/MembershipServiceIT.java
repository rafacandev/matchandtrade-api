package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import com.matchandtrade.persistence.repository.MembershipRepository;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class MembershipServiceIT {

	@Autowired
	private MembershipService fixture;
	private MembershipEntity existingMembership;
	private TradeEntity existingTrade;
	private UserEntity existingUser;
	@Autowired
	private MembershipHelper membershipHelper;
	@Autowired
	private SearchHelper searchHelper;
	@Autowired
	private TradeHelper tradeHelper;
	@Autowired
	private UserHelper userHelper;
	@Autowired
	private MembershipRepository membershipRepository;

	@Before
	public void before() {
		// Build scenario only once for better performance
		if (existingUser == null) {
			existingUser = userHelper.createPersistedEntity();
			existingTrade = tradeHelper.createPersistedEntity(existingUser);
			List<MembershipEntity> tradeMemberships = membershipRepository.findByTrade_TradeId(existingTrade.getTradeId());
			existingMembership = tradeMemberships.stream().filter(membership -> membership.getUser().equals(existingUser)).findFirst().get();
		}
	}

	@Test
	public void findByTradeIdUserIdType_When_TradeIdMatchesUserIdMatchesTypeMatches_Then_1Total() {
		SearchResult<MembershipEntity> actual = fixture.findByTradeIdUserIdType(
			existingTrade.getTradeId(),
			existingUser.getUserId(),
			MembershipEntity.Type.OWNER, 1, 1);
		assertEquals(1, actual.getPagination().getTotal());
		assertEquals(existingMembership, actual.getResultList().get(0));
	}

	@Test
	public void findByTradeIdUserIdType_When_TradeIdMatchesUserIdMatchesTypeDoesNotMatch_Then_0Total() {
		SearchResult<MembershipEntity> actual = fixture.findByTradeIdUserIdType(
			existingTrade.getTradeId(),
			existingUser.getUserId(),
			MembershipEntity.Type.MEMBER, 1, 1);
		assertEquals(0, actual.getPagination().getTotal());
	}

	@Test
	public void findByTradeIdUserIdType_When_TradeIdMatchesUserIdDoesNotMatchTypeMatches_Then_0Total() {
		SearchResult<MembershipEntity> actual = fixture.findByTradeIdUserIdType(
			existingTrade.getTradeId(),
			2,
			MembershipEntity.Type.OWNER, 1, 1);
		assertEquals(0, actual.getPagination().getTotal());
	}

	@Test
	public void findByTradeIdUserIdType_When_TradeIdDoesNotMatchUserIdMatchesTypeMatches_Then_0Total() {
		SearchResult<MembershipEntity> actual = fixture.findByTradeIdUserIdType(
			2,
			existingUser.getUserId(),
			MembershipEntity.Type.OWNER, 1, 1);
		assertEquals(0, actual.getPagination().getTotal());
	}

	@Test
	public void findByTradeIdUserIdType_When_TradeIdIsNullUserIdMatchesTypeMatches_1Total() {
		SearchResult<MembershipEntity> actual = fixture.findByTradeIdUserIdType(
			null,
			existingUser.getUserId(),
			MembershipEntity.Type.OWNER, 1, 1);
		assertEquals(1, actual.getPagination().getTotal());
	}

	@Test
	public void findByTradeIdUserIdType_When_TradeIdMatchesUserIdIsNullTypeMatches_1Total() {
		SearchResult<MembershipEntity> actual = fixture.findByTradeIdUserIdType(
			existingTrade.getTradeId(),
			null,
			MembershipEntity.Type.OWNER, 1, 1);
		assertEquals(1, actual.getPagination().getTotal());
	}

}
