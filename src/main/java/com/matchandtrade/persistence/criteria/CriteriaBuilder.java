package com.matchandtrade.persistence.criteria;

import org.hibernate.Criteria;

import com.matchandtrade.common.SearchCriteria;

public interface CriteriaBuilder {
	
	public Criteria buildSearchCriteria(SearchCriteria searchCriteria);

}
