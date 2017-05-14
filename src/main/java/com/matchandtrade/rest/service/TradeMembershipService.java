package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.repository.TradeMembershipRepository;

@Component
public class TradeMembershipService {

	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;
	
	public void create(TradeMembershipEntity tradeMembership) {
		// Rule, when creating a new TradeMembership it's Type is MEMBER by default
		tradeMembership.setType(TradeMembershipEntity.Type.MEMBER);
		// Delegate to RepositoryLayer
		tradeMembershipRepository.save(tradeMembership);
	}

	public void delete(Integer tradeMembershipId) {
		tradeMembershipRepository.delete(tradeMembershipId);
	}

	public TradeMembershipEntity get(Integer tradeMembershipId) {
		return tradeMembershipRepository.get(tradeMembershipId);
	}

	public SearchResult<TradeMembershipEntity> search(Integer tradeId, Integer userId, Integer _pageNumber, Integer _pageSize) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		if (userId != null) {
			searchCriteria.addCriterion(TradeMembershipEntity.Field.userId, userId);
		}
		if (tradeId != null) {
			searchCriteria.addCriterion(TradeMembershipEntity.Field.tradeId, tradeId);
		}
		// Delegate to Repository layer
		return tradeMembershipRepository.search(searchCriteria);
	}
	
}
