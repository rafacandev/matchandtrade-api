package com.matchandtrade.persistence.criteria;

import static com.matchandtrade.persistence.criteria.QueryBuilderUtil.buildClause;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;

@Component
public class TradeQueryBuilder implements QueryBuilder {

	public enum Field implements com.matchandtrade.persistence.common.Field {
		name("trade.name");
		private String alias;
		Field(String alias) {
			this.alias = alias;
		}
		@Override
		public String toString() {
			return alias;
		}
		@Override
		public String alias() {
			return alias;
		}
	}
	
    @Autowired
    private EntityManager entityManager;
    private static final String BASIC_HQL = "FROM TradeEntity trade";

    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*) " + BASIC_HQL);
    	return parameterizeQuery(searchCriteria, hql);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
		StringBuilder hql = new StringBuilder(BASIC_HQL);
		return parameterizeQuery(searchCriteria, hql);
	}
	
	private Query parameterizeQuery(SearchCriteria searchCriteria, StringBuilder hql) {
		hql.append(" WHERE 1=1");
		
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
