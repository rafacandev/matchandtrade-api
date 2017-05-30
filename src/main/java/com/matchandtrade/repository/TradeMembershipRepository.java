package com.matchandtrade.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.TradeMembershipCriteriaBuilder;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;

@Repository
public class TradeMembershipRepository {

	@Autowired
	private BasicRepository<TradeMembershipEntity> basicRepository;
	@Autowired
	private TradeMembershipCriteriaBuilder criteriaBuilder;
	@Autowired
	private SearchableRepository<TradeMembershipEntity> searchableResposity;

	@Transactional
	public void delete(Integer tradeMembershipId) {
		TradeMembershipEntity tm = get(tradeMembershipId);
		basicRepository.delete(tm);
	}

	@Transactional
	public TradeMembershipEntity get(Integer tradeMembershipId) {
		return basicRepository.get(TradeMembershipEntity.class, tradeMembershipId);
	}

	@Transactional
	public void save(TradeMembershipEntity entity) {
		basicRepository.save(entity);
	}
	
	@Transactional
	public SearchResult<TradeMembershipEntity> search(SearchCriteria searchCriteria) {
		return searchableResposity.search(searchCriteria, criteriaBuilder);
	}

}
