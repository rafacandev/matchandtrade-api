package com.matchandtrade.rest.v1.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.rest.RestException;

@Component
public class PaginationValidator {
	
	//Utility classes should not have public constructors
	PaginationValidator() { }

	public static void validatePageNumberAndPageSize(Integer pageNumber, Integer pageSize) {
		if (pageNumber != null && pageNumber < 0) {
			throw new RestException(HttpStatus.BAD_REQUEST, "_pageNumber must be greater than 0 when present.");
		}
		if (pageSize != null && (pageSize < 0 || pageSize > 50)) {
			throw new RestException(HttpStatus.BAD_REQUEST, "_pageSize must be between 0 and 50 when present.");
		}
	}

}
