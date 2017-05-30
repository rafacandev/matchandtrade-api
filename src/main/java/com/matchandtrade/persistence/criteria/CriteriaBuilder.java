package com.matchandtrade.persistence.criteria;

import org.hibernate.Criteria;

import com.matchandtrade.persistence.common.SearchCriteria;

public interface CriteriaBuilder {
	
	public Criteria buildSearchCriteria(SearchCriteria searchCriteria);

}
