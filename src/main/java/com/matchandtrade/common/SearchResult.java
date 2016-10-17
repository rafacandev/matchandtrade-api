package com.matchandtrade.common;

import java.util.List;

public class SearchResult<T> {
	private Pagination pagination;
//	@ApiModelProperty(name="data")
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

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}

}
