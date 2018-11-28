package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.ArticleHelper;
import com.matchandtrade.test.helper.MembershipHelper;
import com.matchandtrade.test.helper.UserHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class MembershipServiceIT {

	@Autowired
	private MembershipService fixture;
	private MembershipEntity existingMembership;
	private UserEntity existingUser;
	@Autowired
	private MembershipHelper membershipHelper;
	@Autowired
	private UserHelper userHelper;

	@Before
	public void before() {
		existingUser = userHelper.createPersistedEntity();
		existingMembership = membershipHelper.createPersistedEntity(existingUser);

	}

	@Test
	public void get_When_findByUserIdAndMembershipIdWithNullAndNull_Then_Returns0Results() {
		SearchResult<MembershipEntity> actual = fixture.findByUserIdAndMembershpiId(null, null);
		assertEquals(0, actual.getPagination().getTotal());
	}

	@Test
	public void get_When_findByUserIdAndMembershipIdWithNotMatchAndNotMatch_Then_Returns0Results() {
		SearchResult<MembershipEntity> actual = fixture.findByUserIdAndMembershpiId(-1, -1);
		assertEquals(0, actual.getPagination().getTotal());
	}

	@Test
	public void get_When_findByUserIdAndMembershipIdWithMatchAndNotMatch_Then_Returns0Results() {
		SearchResult<MembershipEntity> actual = fixture.findByUserIdAndMembershpiId(existingUser.getUserId(), -1);
		assertEquals(0, actual.getPagination().getTotal());
	}

	@Test
	public void get_When_findByUserIdAndMembershipIdWithNotMatchAndMatch_Then_Returns0Results() {
		SearchResult<MembershipEntity> actual = fixture.findByUserIdAndMembershpiId(-1, existingMembership.getMembershipId());
		assertEquals(0, actual.getPagination().getTotal());
	}

	@Test
	public void get_When_findByUserIdAndMembershipIdWithMatchAndMatch_Then_Returns1Result() {
		SearchResult<MembershipEntity> actual = fixture.findByUserIdAndMembershpiId(existingUser.getUserId(), existingMembership.getMembershipId());
		assertEquals(1, actual.getPagination().getTotal());
	}

}
