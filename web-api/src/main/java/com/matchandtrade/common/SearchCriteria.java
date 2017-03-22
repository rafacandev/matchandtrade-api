package com.matchandtrade.common;

import java.util.ArrayList;
import java.util.List;

public class SearchCriteria {
	public enum OrderBy {
		ASC,DESC
	}
	private List<Criterion> criteria = new ArrayList<Criterion>();
	private Pagination pagination;
	
	public SearchCriteria(Pagination pagination) {
		this.pagination = pagination;
	}
	
	public void addCriterion(Criterion criterion) {
		this.criteria.add(criterion);
	}
	
	public void addCriterion(Object field, Object value) {
		Criterion c = new Criterion(field, value);
		this.criteria.add(c);
	}
	
	public List<Criterion> getCriteria() {
		return criteria;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setCriteria(List<Criterion> criteria) {
		this.criteria = criteria;
	}

}
