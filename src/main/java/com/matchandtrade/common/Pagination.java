package com.matchandtrade.common;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Data structure to store pagination values.
 * @author rafael.santos.bra@gmail.com
 */
public class Pagination {
	private Integer limit;
	private Integer page;
	private Long total;

	/**
	 * Page and limit must be a value greater than zero value
	 * @param page
	 * @param limit
	 * @param total
	 */
	public Pagination(Integer page, Integer limit, Long total) {
		if (page != null && page > 0) {
			this.page = page;
		} else {
			this.page = 1;
		}
		if (limit != null && limit > 0) {
			this.limit = limit;
		} else {
			this.limit = 10;
		}
		this.total = total;
	}

	/**
	 * Page and limit must be a greater than zero value
	 * @param page
	 * @param limit
	 */
	public Pagination(Integer page, Integer limit) {
		this(page, limit, null);
	}

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public Integer getLimit() {
		return limit;
	}

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public Integer getPage() {
		return page;
	}

	public Long getTotal() {
		return total;
	}

}
