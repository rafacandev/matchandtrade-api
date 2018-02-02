package com.matchandtrade.rest.v1.json.search;

public class Criterion {
	private String key;
	private Object value;
	private Operator operator = Operator.AND;
	private Matcher matcher = Matcher.EQUALS;

	// Empty constructor required by Jackson serialization
	public Criterion() { }
	
	Criterion(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	Criterion(String key, Object value, Operator operator) {
		this.key = key;
		this.value = value;
		this.operator = operator;
	}

	Criterion(String key, Object value, Operator operator, Matcher restriction) {
		this.key = key;
		this.value = value;
		this.operator = operator;
		this.matcher = restriction;
	}
	
	public String getKey() {
		return key;
	}
	public Matcher getMatcher() {
		return matcher;
	}
	public Operator getOperator() {
		return operator;
	}
	public Object getValue() {
		return value;
	}

}