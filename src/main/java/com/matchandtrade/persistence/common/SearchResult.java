package com.matchandtrade.persistence.common;

import java.util.List;

/**
 * POJO for search results.
 * 
 * @author rafael.santos.bra@gmail.com
 * @param <T>
 */
public class SearchResult<T> {

	private Pagination pagination;
	private List<T> resultList;
	
	public SearchResult(List<T> resultList, Pagination pagination) {
		this.resultList = resultList;
		this.pagination = pagination;
	}

	public Pagination getPagination() {
		return pagination;
	}
	
	public List<T> getResultList() {
		return resultList;
	}

}
