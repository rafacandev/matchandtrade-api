package com.matchandtrade.persistence.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Search Criteria to be used with QueryBuilders
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
public class SearchCriteria {

	private List<Criterion> criteria = new ArrayList<>();
	private List<Sort> sorts = new ArrayList<>();
	private Pagination pagination;
	
	public SearchCriteria(Pagination pagination) {
		this.pagination = pagination;
	}
	
	public void addCriterion(Field field, Object value) {
		Criterion c = new Criterion(field, value);
		this.criteria.add(c);
	}

	public void addCriterion(Field field, Object value, Criterion.Restriction restriction) {
		Criterion c = new Criterion(field, value, restriction);
		this.criteria.add(c);
	}

	public void addSort(Sort sort) {
		this.sorts.add(sort);
	}

	public List<Criterion> getCriteria() {
		return criteria;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public List<Sort> getSorts() {
		return sorts;
	}
}
