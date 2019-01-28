package com.matchandtrade.rest.v1.json.search;

public class SortJson {
	public enum Type {
		ASC, DESC
	}

	private String field;
	private Type type;

	public String getField() {
		return field;
	}

	public Type getType() {
		return type;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
