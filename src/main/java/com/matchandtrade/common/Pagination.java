package com.matchandtrade.common;

/**
 * POJO to keep pagination values.
 * @author rafael.santos.bra@gmail.com
 */
public class Pagination {
	private int size = 10;
	private int number = 0;
	private long total = 0;

	public Pagination() {
		super();
	}
	
	public Pagination(Integer number, Integer size) {
		if (number != null) {
			this.number = number;
		}
		if (size != null) {
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
	 * Get the page size.
	 * @return
	 */
	public int getSize() {
		return size;
	}
	/**
	 * Set the page size.
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
	}
	
	/**
	 * Get the page number starting on zero.
	 * @return
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * Set the page number.
	 * @param number
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * Get the total of records.
	 * @return
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
	
}