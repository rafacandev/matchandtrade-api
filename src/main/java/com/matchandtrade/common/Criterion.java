package com.matchandtrade.common;

/**
 * A criterion, typically used with {@code SearchCriteria}
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
public class Criterion {
	private Object field;
	private Object value;

	public Criterion(Object field, Object value) {
		this.field = field;
		this.value = value;
	}

	public Object getField() {
		return field;
	}

	public Object getValue() {
		return value;
	}

}
