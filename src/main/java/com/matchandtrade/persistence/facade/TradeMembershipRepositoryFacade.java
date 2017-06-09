package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.TradeMembershipQueryBuilderJavax;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.repository.TradeMembershipRepository;

@Repository
public class TradeMembershipRepositoryFacade {

	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;
	@Autowired
	private TradeMembershipQueryBuilderJavax tradeMembershipQueryBuilder;
	@Autowired
	private QueryableRepositoryJavax<TradeMembershipEntity> queriableResposity;

	public void delete(Integer tradeMembershipId) {
		tradeMembershipRepository.delete(tradeMembershipId);
	}

	public TradeMembershipEntity get(Integer tradeMembershipId) {
		return tradeMembershipRepository.findOne(tradeMembershipId);
	}

	public void save(TradeMembershipEntity entity) {
		tradeMembershipRepository.save(entity);
	}
	
	public SearchResult<TradeMembershipEntity> search(SearchCriteria searchCriteria) {
		return queriableResposity.query(searchCriteria, tradeMembershipQueryBuilder);
	}

}
