package com.matchandtrade.persistence.common;

/**
 * {@code Field}s are used in conjunction to {@code Criteria} and used in parameterized queries.
 * 
 * Example: Imagine the following JPA Query
 * <pre>
 *     SELECT i.name FROM ItemEntity AS i.name=:itemName;
 *     -- i.name is the alias
 *     -- itemName is the param
 * <pre>
 * @return
 */
public interface Field {
	
	/**
	 * The alias for this field
	 * @return
	 */
	String alias();
	
	/**
	 * The parameter for this field
	 * @return
	 */
	String name();
	
}
