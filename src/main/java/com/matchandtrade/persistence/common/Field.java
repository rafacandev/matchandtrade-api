package com.matchandtrade.persistence.common;

/**
 * {@code Field}s are used in conjunction to {@code Criteria} and used in parameterized queries.
 * 
 * Example: Imagine the following JPA Query
 * <pre>
 *     SELECT trade.name FROM TradeEntity AS trade WHERE trade.name=:tradeName;
 *     -- trade.name is the alias
 * <pre>
 * @return
 */
public interface Field {
	
	/**
	 * The alias for this field
	 * @return
	 */
	String alias();
	
}
