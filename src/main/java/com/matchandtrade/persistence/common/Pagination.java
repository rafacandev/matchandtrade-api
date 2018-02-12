package com.matchandtrade.persistence.common;

import org.springframework.http.HttpStatus;

import com.matchandtrade.rest.RestException;

/**
 * POJO to keep pagination values.
 * @author rafael.santos.bra@gmail.com
 */
public class Pagination {
	
	public enum Parameter {
		FIRST("_firstPage"), LAST("_lastPage"), NUMBER("_pageNumber"), SIZE("_pageSize"), TOTAL_COUNT("_totalCount");
	    
		private final String text;
	    
	    private Parameter(final String text) {
	        this.text = text;
	    }

	    @Override
	    public String toString() {
	        return text;
	    }
	}
	
	private int size = 10;
	private int number = 1;
	private long total = 0;

	public Pagination() {
		super();
	}
	
	public Pagination(Integer number, Integer size) {
		if (number != null) {
			// TODO throw invalidArgumentException if number < 0, but need to handle it on RestResponseAdvice
			this.number = number;
		}
		if (size != null){
			// TODO throw invalidArgumentException if number < 1, but need to handle it on RestResponseAdvice
			if (size > 50) {
				// TODO throw invalidArgumentException, but need to handle it on RestResponseAdvice
				throw new RestException(HttpStatus.BAD_REQUEST, "_pageSize cannot be bigger than 50.");
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
	 * Get the page number starting on zero.
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