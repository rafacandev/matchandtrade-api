package com.matchandtrade.persistence.criteria;

import com.matchandtrade.persistence.common.SearchCriteria;

import javax.persistence.Query;

public interface QueryBuilder {

	Query buildSearchQuery(SearchCriteria searchCriteria);

	Query buildCountQuery(SearchCriteria searchCriteria);

}
