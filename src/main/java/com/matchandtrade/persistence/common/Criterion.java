package com.matchandtrade.persistence.common;

/**
 * A criterion, typically used with {@code SearchCriteria}
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
public class Criterion {

	public enum Restriction {EQUALS, NOT_EQUALS, EQUALS_IGNORE_CASE, LIKE_IGNORE_CASE}
	public enum LogicalOperator {AND, OR}
	
	private Field field;
	private LogicalOperator logicalOperator;
	private Restriction restriction;
	private Object value;
	
	public Criterion(Field field, Object value) {
		this.field = field;
		this.value = value;
		this.logicalOperator = LogicalOperator.AND;
		this.restriction = Restriction.EQUALS;
	}

	public Criterion(Field field, Object value, Restriction restriction) {
		this.field = field;
		this.value = value;
		this.logicalOperator = LogicalOperator.AND;
		this.restriction = restriction;
	}

	public Field getField() {
		return field;
	}
	
	public LogicalOperator getLogicalOperator() {
		return logicalOperator;
	}
	
	public Restriction getRestriction() {
		return restriction;
	}

	public Object getValue() {
		return value;
	}

}
