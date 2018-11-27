package com.matchandtrade.rest.v1.validator;

import org.springframework.http.HttpStatus;

import com.matchandtrade.rest.RestException;

public class PaginationValidator {
	
	//Utility classes should not have public constructors
	PaginationValidator() { }

	public static void validatePageNumberAndPageSize(Integer pageNumber, Integer pageSize) {
		if (pageNumber != null && pageNumber < 1) {
			throw new RestException(HttpStatus.BAD_REQUEST, "PageNumber must be greater than 0 when present");
		}
		if (pageSize != null && (pageSize < 1 || pageSize > 50)) {
			throw new RestException(HttpStatus.BAD_REQUEST, "PageSize must be between 1 and 50 when present");
		}
	}

}
