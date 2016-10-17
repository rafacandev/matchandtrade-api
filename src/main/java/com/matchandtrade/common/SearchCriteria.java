package com.matchandtrade.common;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;

public class SearchCriteria {
	public enum OrderBy {
		ASC,DESC
	}
	private List<Criterion> criteria = new ArrayList<Criterion>();
	private Pagination pagination;
	private List<Order> orderBy = new ArrayList<>();
	
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
	
	public void addOrderBy(Order order) {
		this.orderBy.add(order);
	}

	public List<Criterion> getCriteria() {
		return criteria;
	}

	public List<Order> getOrderBy() {
		return orderBy;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setCriteria(List<Criterion> criteria) {
		this.criteria = criteria;
	}

	public void setOrderBy(List<Order> orderBy) {
		this.orderBy = orderBy;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

}
