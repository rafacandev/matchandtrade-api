package com.matchandtrade.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.dao.TradeMembershipDao;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;

@Repository
public class TradeMembershipRepository {

	@Autowired
	private TradeMembershipDao tradeMembershipDao;

	@Transactional
	public TradeMembershipEntity get(Integer tradeMembershipId) {
		return tradeMembershipDao.get(TradeMembershipEntity.class, tradeMembershipId);
	}

	@Transactional
	public void save(TradeMembershipEntity entity) {
		tradeMembershipDao.save(entity);
	}
	
	@Transactional
	public SearchResult<TradeMembershipEntity> search(SearchCriteria searchCriteria) {
		return tradeMembershipDao.search(searchCriteria);
	}

	@Transactional
	public void delete(Integer tradeMembershipId) {
		TradeMembershipEntity tm = get(tradeMembershipId);
		tradeMembershipDao.delete(tm);
	}

}
