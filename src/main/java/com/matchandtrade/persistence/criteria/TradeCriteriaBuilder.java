package com.matchandtrade.persistence.criteria;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.Criterion;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.persistence.entity.TradeEntity;

@Component
public class TradeCriteriaBuilder implements CriteriaBuilder {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
	public Criteria buildSearchCriteria(SearchCriteria searchCriteria) {
		Criteria result = sessionFactory.getCurrentSession().createCriteria(TradeEntity.class);
		// Add Criterion
		for (Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(TradeEntity.Field.name)) {
				result.add(Restrictions.eq(TradeEntity.Field.name.toString(), c.getValue()));
			}
		}
		return result;
	}
	
}
