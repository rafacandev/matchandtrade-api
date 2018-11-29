package com.matchandtrade.test.helper;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Transactional
@Commit
public class SearchHelper {

	@Autowired
	private MembershipService membershipService;

	public MembershipEntity findMembership(UserEntity user, TradeEntity trade) {
		SearchResult<MembershipEntity> searchResult = membershipService.findByTradeIdUserIdType(
			trade.getTradeId(),
			user.getUserId(),
			null,
			1,
			1);
		MembershipEntity persistedEntity = searchResult.getResultList().get(0);
		MembershipEntity result = new MembershipEntity();
		result.setMembershipId(persistedEntity.getMembershipId());
		result.setType(persistedEntity.getType());
		result.setUser(user);
		result.setTrade(trade);
		return result;
	}

	public static <T> SearchResult<T> buildEmptySearchResult() {
		return new SearchResult<>(Collections.emptyList(), new Pagination(1, 1, 0L));
	}

	public static <T> SearchResult<T> buildSearchResult(List<T> resultList) {
		return new SearchResult<>(resultList, new Pagination(1, 1, (long) resultList.size()));
	}

	public static <T> SearchResult<T> buildSearchResult(T... results) {
		return new SearchResult<>(Arrays.asList(results), new Pagination(1, 1, (long) results.length));
	}

}
