package com.matchandtrade.persistence.criteria;

import javax.persistence.Query;

import com.matchandtrade.persistence.common.SearchCriteria;

public interface QueryBuilderJavax {

	public Query buildSearchQuery(SearchCriteria searchCriteria);

	public Query buildCountQuery(SearchCriteria searchCriteria);

}
