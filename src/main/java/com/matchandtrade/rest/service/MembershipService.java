package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.MembershipQueryBuilder;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;

@Component
public class MembershipService {

	@Autowired
	private SearchService searchService;
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

	public MembershipEntity get(Integer membershipId) {
		return membershipRepositoryFacade.get(membershipId);
	}

	public SearchResult<MembershipEntity> searchByTradeIdUserIdType(Integer tradeId, Integer userId, MembershipEntity.Type type, Integer _pageNumber, Integer _pageSize) {
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

}
