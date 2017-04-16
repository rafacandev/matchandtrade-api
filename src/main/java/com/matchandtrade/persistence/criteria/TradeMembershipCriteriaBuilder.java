package com.matchandtrade.persistence.criteria;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.Criterion;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;

@Component
public class TradeMembershipCriteriaBuilder implements CriteriaBuilder {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
	public Criteria buildSearchCriteria(SearchCriteria searchCriteria) {
		Criteria result = sessionFactory.getCurrentSession().createCriteria(TradeMembershipEntity.class);
		// Add Criterion
		for (Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(TradeMembershipEntity.Field.tradeId)) {
				result.add(Restrictions.eq(TradeMembershipEntity.Field.tradeId.toString(), c.getValue()));
			}
			if (c.getField().equals(TradeMembershipEntity.Field.userId)) {
				result.add(Restrictions.eq(TradeMembershipEntity.Field.userId.toString(), c.getValue()));
			}
			if (c.getField().equals(TradeMembershipEntity.Field.tradeMembershipId)) {
				result.add(Restrictions.eq(TradeMembershipEntity.Field.tradeMembershipId.toString(), c.getValue()));
			}
		}
		return result;
	}
	
}
