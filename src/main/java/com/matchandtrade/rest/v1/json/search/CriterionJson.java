package com.matchandtrade.rest.v1.json.search;

public class CriterionJson {
	private String field;
	private Object value;
	private Operator operator = Operator.AND;
	private Matcher matcher = Matcher.EQUALS;

	// Empty constructor required by Jackson serialization
	public CriterionJson() { }
	
	CriterionJson(String field, Object value) {
		this.field = field;
		this.value = value;
	}

	CriterionJson(String field, Object value, Operator operator) {
		this.field = field;
		this.value = value;
		this.operator = operator;
	}

	CriterionJson(String field, Object value, Operator operator, Matcher restriction) {
		this.field = field;
		this.value = value;
		this.operator = operator;
		this.matcher = restriction;
	}
	
	public String getField() {
		return field;
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