package com.matchandtrade.persistence.criteria;

import javax.persistence.Query;

import com.matchandtrade.persistence.common.SearchCriteria;

public interface QueryBuilder {

	Query buildSearchQuery(SearchCriteria searchCriteria);

	Query buildCountQuery(SearchCriteria searchCriteria);

}
