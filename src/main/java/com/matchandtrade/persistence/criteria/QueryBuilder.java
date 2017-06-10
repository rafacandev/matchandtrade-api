package com.matchandtrade.persistence.criteria;

import javax.persistence.Query;

import com.matchandtrade.persistence.common.SearchCriteria;

public interface QueryBuilder {

	public Query buildSearchQuery(SearchCriteria searchCriteria);

	public Query buildCountQuery(SearchCriteria searchCriteria);

}
