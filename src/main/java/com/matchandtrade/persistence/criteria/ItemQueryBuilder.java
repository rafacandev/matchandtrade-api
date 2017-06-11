package com.matchandtrade.persistence.criteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;
import static com.matchandtrade.persistence.criteria.QueryBuilderUtil.*;

@Component
public class ItemQueryBuilder implements QueryBuilder {

	public enum Field implements com.matchandtrade.persistence.common.Field {
		itemIdIsNot("item.itemId"),
		name("item.name"),
		tradeId("trade.tradeId"),
		tradeMembershipId("tm.tradeMembershipId");
		
		private String alias;

		private Field(String alias) {
			this.alias = alias;
		}
		
		@Override
		public String alias() {
			return alias;
		}
	}
	
	@Autowired
	private EntityManager entityManager;
	
    private static final String BASIC_HQL = "FROM TradeMembershipEntity AS tm"
    		+ " INNER JOIN tm.trade AS trade"
    		+ " INNER JOIN tm.items AS item"
    		+ " WHERE 1=1 ";

    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*) " + BASIC_HQL);
    	return parameterizeQuery(searchCriteria, hql);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT item " + BASIC_HQL);
		return parameterizeQuery(searchCriteria, hql);
	}

	private Query parameterizeQuery(SearchCriteria searchCriteria, StringBuilder hql) {
		// Add Field
		for (com.matchandtrade.persistence.common.Criterion c : searchCriteria.getCriteria()) {
			Field field = (Field) c.getField();
			hql.append(buildClause(field, c));
		}
		Query result = entityManager.createQuery(hql.toString());
		for (com.matchandtrade.persistence.common.Criterion c : searchCriteria.getCriteria()) {
			Field field = (Field) c.getField();
			result.setParameter(field.name(), c.getValue());
		}
		return result;
	}

}
