package com.matchandtrade.persistence.criteria;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.persistence.entity.TradeEntity;

@Component
public class TradeCriteriaBuilder implements CriteriaBuilder {

	public enum Criterion {
		name
	}
	
    @Autowired
    private SessionFactory sessionFactory;

    @Override
	public Criteria buildSearchCriteria(SearchCriteria searchCriteria) {
		Criteria result = sessionFactory.getCurrentSession().createCriteria(TradeEntity.class);
		// Add Criterion
		for (com.matchandtrade.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.name)) {
				result.add(Restrictions.eq("name", c.getValue()));
			}
		}
		return result;
	}
	
}
