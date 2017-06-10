package com.matchandtrade.persistence.criteria;

import static com.matchandtrade.persistence.criteria.QueryBuilderUtil.buildClauses;
import static com.matchandtrade.persistence.criteria.QueryBuilderUtil.setParameters;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;

@Component
public class WantItemQueryBuilder implements QueryBuilder {

	public enum Criterion {
		itemId("item.itemId"), priority("wantItem.priority");
		private String alias;
		Criterion(String alias) {
			this.alias = alias;
		}
		@Override
		public String toString() {
			return alias;
		}
	}
	
    @Autowired
    private EntityManager entityManager;
    
    private static final String BASIC_HQL = " FROM WantItemEntity AS wantItem"
    		+ " INNER JOIN wantItem.item AS item";

    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*)" + BASIC_HQL);
    	return parameterizeQuery(searchCriteria, hql);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder(BASIC_HQL);
		return parameterizeQuery(searchCriteria, hql);
	}

	private Query parameterizeQuery(SearchCriteria searchCriteria, StringBuilder hql) {
		hql.append(buildClauses(searchCriteria));
		Query result = entityManager.createQuery(hql.toString());
		setParameters(searchCriteria, result);
		return result;
	}

}
