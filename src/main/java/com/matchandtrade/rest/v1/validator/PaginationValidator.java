package com.matchandtrade.rest.v1.validator;

import org.springframework.http.HttpStatus;

import com.matchandtrade.rest.RestException;

public class PaginationValidator {

	public static void validatePageSize(Integer _pageSize) {
		if (_pageSize != null && _pageSize > 50) {
			throw new RestException(HttpStatus.BAD_REQUEST, "_pageSize cannot be bigger than 50.");
		}
	}

}
