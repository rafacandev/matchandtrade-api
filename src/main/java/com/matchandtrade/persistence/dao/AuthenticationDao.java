package com.matchandtrade.persistence.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.RootEntityResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.Criterion;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;


@Component
public class AuthenticationDao extends Dao<AuthenticationEntity> {

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	SearchDao<AuthenticationEntity> searchableDao;
	
	protected Criteria buildSearchCriteria(SearchCriteria searchCriteria) {
		Criteria result = getCurrentSession().createCriteria(AuthenticationEntity.class);
		// Add Criterion
		for (Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(AuthenticationEntity.Field.token)) {
				result.add(Restrictions.eq(AuthenticationEntity.Field.token.toString(), c.getValue()));
			}
		}
		return result;
	}
	
	public SearchResult<AuthenticationEntity> search(SearchCriteria searchCriteria) {
		Criteria criteria = buildSearchCriteria(searchCriteria);
		// Set pagination total
		long rowCount = SearchDao.rowCount(criteria);
		searchCriteria.getPagination().setTotal(rowCount);
		// Search
		List<AuthenticationEntity> resultList = searchableDao.search(
				buildSearchCriteria(searchCriteria),
				searchCriteria.getPagination(),
				RootEntityResultTransformer.INSTANCE);
		return new SearchResult<>(resultList, searchCriteria.getPagination());
	}

}
