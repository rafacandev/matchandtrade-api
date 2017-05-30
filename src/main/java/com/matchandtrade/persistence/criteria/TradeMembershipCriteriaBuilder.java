package com.matchandtrade.persistence.criteria;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;

@Component
public class TradeMembershipCriteriaBuilder implements CriteriaBuilder {

	public enum Criterion {
		userId, tradeId, tradeMembershipId, type, itemId
	}
	
    @Autowired
    private SessionFactory sessionFactory;

    @Override
	public Criteria buildSearchCriteria(SearchCriteria searchCriteria) {
		Criteria result = sessionFactory.getCurrentSession().createCriteria(TradeMembershipEntity.class);
		// Add Criterion
		for (com.matchandtrade.persistence.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.tradeId)) {
				result.add(Restrictions.eq("trade.tradeId", c.getValue()));
			}
			if (c.getField().equals(Criterion.userId)) {
				result.add(Restrictions.eq("user.userId", c.getValue()));
			}
			if (c.getField().equals(Criterion.type)) {
				result.add(Restrictions.eq("type", c.getValue()));
			}
			if (c.getField().equals(Criterion.itemId)) {
				result.createAlias("items", "item");
				result.add(Restrictions.eq("item.itemId", c.getValue()));
			}
			if (c.getField().equals(Criterion.tradeMembershipId)) {
				if (c.getRestriction().equals(com.matchandtrade.persistence.common.Criterion.Restriction.NOT_EQUALS)) {
					result.add(Restrictions.ne("tradeMembershipId", c.getValue()));
				} else if (c.getRestriction().equals(com.matchandtrade.persistence.common.Criterion.Restriction.EQUALS)) {
					result.add(Restrictions.eq("tradeMembershipId", c.getValue()));
				}
			}
		}
		return result;
	}
	
}
