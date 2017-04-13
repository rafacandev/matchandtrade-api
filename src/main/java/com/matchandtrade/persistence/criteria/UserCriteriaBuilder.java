package com.matchandtrade.persistence.criteria;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.Criterion;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.persistence.entity.UserEntity;

@Component
public class UserCriteriaBuilder implements CriteriaBuilder {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
	public Criteria buildSearchCriteria(SearchCriteria searchCriteria) {
		Criteria result = sessionFactory.getCurrentSession().createCriteria(UserEntity.class);
		// Add Criterion
		for (Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(UserEntity.Field.email)) {
				result.add(Restrictions.eq(UserEntity.Field.email.toString(), c.getValue()));
			}
		}
		return result;
	}
	
}
