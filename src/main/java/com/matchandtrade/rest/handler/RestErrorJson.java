package com.matchandtrade.rest.handler;

import java.util.ArrayList;
import java.util.List;

public class RestErrorJson {

	private List<RestError> errors = new ArrayList<>();

	public List<RestError> getErrors() {
		return errors;
	}
}