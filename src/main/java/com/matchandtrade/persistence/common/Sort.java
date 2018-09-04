package com.matchandtrade.persistence.common;

/**
 * <p>
 * {@code Sort} is used in conjunction to {@code Criteria} and used to indicate how to sort the results.
 * </p>
 * 
 * {@code field} indicates which field alias to use which varies according to which {@code QueryableRepository} is begin used.
 * {@code type} indicates which sorting type to use (e.g.: ASC, DESC).  
 * 
 * Example: Imagine the following JPA Query
 * <pre>
 *     FROM TradEntity AS trade ORDER BY trade.name ASC;
 *     -- trade.name is the field
 *     -- ASC is the type
 * <pre>
 * @return
 */
public class Sort {
	
	public enum Type {
		ASC, DESC;
	}

	private Field field;
	private Type type;

	public Sort(Field field, Type type) {
		this.field = field;
		this.type = type;
	}
	
	public Field field() {
		return field;
	}
	
	public Type type() {
		return type;
	}
	
}

