package com.matchandtrade.persistence.common;

/**
 * <p>
 * {@code Order}s are used in conjunction to {@code Criteria} and used to indicate how to sort the results.
 * </p>
 * 
 * {@code alias} indicates which field alias to use which varies according to which {@code QueryableRepository} is begin used.
 * {@code sortingType} indicates which sorting type to use (e.g.: ASC, DESC).  
 * 
 * Example: Imagine the following JPA Query
 * <pre>
 *     FROM ItemEntity AS i ORDER BY i.name ASC;
 *     -- i.name is the alias
 *     -- ASC is the sortingType
 * <pre>
 * @return
 */
public class Order {

	private String alias;
	private SortingType sortingType;

	public Order(String alias, SortingType sortingType) {
		this.alias = alias;
		this.sortingType = sortingType;
	}

	public String alias() {
		return alias;
	}

	public SortingType sortingType() {
		return sortingType;
	}

}

