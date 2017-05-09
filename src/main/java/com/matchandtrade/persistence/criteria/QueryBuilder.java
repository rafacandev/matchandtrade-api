package com.matchandtrade.persistence.criteria;

import org.hibernate.Query;

import com.matchandtrade.common.SearchCriteria;

public interface QueryBuilder {
	
	public Query buildSearchQuery(SearchCriteria searchCriteria);
	public Query buildCountQuery(SearchCriteria searchCriteria);

}
