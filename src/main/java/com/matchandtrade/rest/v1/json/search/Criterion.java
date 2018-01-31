package com.matchandtrade.rest.v1.json.search;

public class Criterion {
	private String key;
	private Object value;

	// Empty constructor required by Jackson serialization
	public Criterion() { }
	
	Criterion(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
}