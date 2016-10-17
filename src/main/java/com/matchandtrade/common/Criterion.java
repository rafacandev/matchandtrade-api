package com.matchandtrade.common;

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

	public void setField(Object field) {
		this.field = field;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
