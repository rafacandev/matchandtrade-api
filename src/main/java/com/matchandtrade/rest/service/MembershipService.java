package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.MembershipQueryBuilder;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MembershipService {

	@Autowired
	private SearchService<MembershipEntity> searchService;
	@Autowired
	private MembershipRepositoryFacade membershipRepositoryFacade;
	
	public void create(MembershipEntity membership) {
		// Rule, when creating a new Membership it's Type is MEMBER by default
		membership.setType(MembershipEntity.Type.MEMBER);
		// Delegate to RepositoryLayer
		membershipRepositoryFacade.save(membership);
	}

	public void delete(Integer membershipId) {
		membershipRepositoryFacade.delete(membershipId);
	}

	public MembershipEntity find(Integer membershipId) {
		return membershipRepositoryFacade.find(membershipId);
	}

	public SearchResult<MembershipEntity> findByTradeIdUserIdType(Integer tradeId, Integer userId, MembershipEntity.Type type, Integer _pageNumber, Integer _pageSize) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		if (userId != null) {
			searchCriteria.addCriterion(MembershipQueryBuilder.Field.USER_ID, userId);
		}
		if (tradeId != null) {
			searchCriteria.addCriterion(MembershipQueryBuilder.Field.TRADE_ID, tradeId);
		}
		if (type != null) {
			searchCriteria.addCriterion(MembershipQueryBuilder.Field.TYPE, type);
		}
		// Delegate to Repository layer
		return searchService.search(searchCriteria, MembershipQueryBuilder.class);
	}

	public SearchResult<MembershipEntity> findByUserIdAndMembershpiId(Integer userId, Integer membershipId) {
		SearchCriteria criteria = new SearchCriteria(new Pagination());
		criteria.addCriterion(MembershipQueryBuilder.Field.USER_ID, userId);
		criteria.addCriterion(MembershipQueryBuilder.Field.MEMBERSHIP_ID, membershipId);
		return searchService.search(criteria, MembershipQueryBuilder.class);
	}

}
