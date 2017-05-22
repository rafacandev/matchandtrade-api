package com.matchandtrade.persistence.criteria;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;

@Component
public class TradeMembershipCriteriaBuilder implements CriteriaBuilder {

	public enum Criterion {
		userId, tradeId, tradeMembershipId, type
	}
	
    @Autowired
    private SessionFactory sessionFactory;

    @Override
	public Criteria buildSearchCriteria(SearchCriteria searchCriteria) {
		Criteria result = sessionFactory.getCurrentSession().createCriteria(TradeMembershipEntity.class);
		// Add Criterion
		for (com.matchandtrade.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.tradeId)) {
				result.add(Restrictions.eq("trade.tradeId", c.getValue()));
			}
			if (c.getField().equals(Criterion.userId)) {
				result.add(Restrictions.eq("user.userId", c.getValue()));
			}
			if (c.getField().equals(Criterion.type)) {
				result.add(Restrictions.eq("type", c.getValue()));
			}
		}
		return result;
	}
	
}
