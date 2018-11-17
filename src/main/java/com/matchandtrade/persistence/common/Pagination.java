package com.matchandtrade.persistence.common;

import org.springframework.http.HttpStatus;

import com.matchandtrade.rest.RestException;

/**
 * POJO to keep pagination values.
 *
 * @author rafael.santos.bra@gmail.com
 */
public class Pagination {

	private int size = 10;
	private int number = 1;
	private long total = 0;

	public enum Parameter {
		NUMBER("_pageNumber"), SIZE("_pageSize"), TOTAL_COUNT("_totalCount");
	    
		private final String text;
	    
	    Parameter(final String text) {
	        this.text = text;
	    }

	    @Override
	    public String toString() {
	        return text;
	    }
	}

	public Pagination() {
		super();
	}
	
	public Pagination(Integer number, Integer size) {
		if (number != null) {
			if (number < 1) {
				throw new IllegalArgumentException("Pagination number must be greater than 0. Actual value: " + number);
			}
			this.number = number;
		}
		if (size != null){
			if (size < 1 || size > 50) {
				throw new IllegalArgumentException("Pagination size must be between 1 and 50. Actual value: " + size);
			}
			this.size = size;
		}
	}
	
	public Pagination(Integer number, Integer size, Long total ) {
		this(number, size);
		if (total != null) {
			this.total = total;
		}
	}
	
	/**
	 * Returns true if there is a next page. Formally, it returns {@code ((float) total / size) > number}.
	 * @return true if has a next page; false otherwise.
	 */
	public boolean hasNextPage() {
		return ((float) total / size) > number;
	}

	/**
	 * Get the page size.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Get the page number starting on one.
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * Get the total of records.
	 */
	public long getTotal() {
		return total;
	}
	
	/**
	 * Set the total of records
	 *
	 * @param total
	 */
	public void setTotal(long total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "Pagination [size=" + size + ", number=" + number + ", total=" + total + "]";
	}
	
}