package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.rest.RestException;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;

public class PaginationValidatorUT {

	@Test
	public void validatePageNumberAndPageSize_When_PageNumberIs1AndPageSizeIs1_Then_Succeeds() {
		PaginationValidator.validatePageNumberAndPageSize(1, 1);
	}

	@Test
	public void validatePageNumberAndPageSize_When_PageNumberIsNullAndPageSizeIs1_Then_Succeeds() {
		PaginationValidator.validatePageNumberAndPageSize(null, 1);
	}

	@Test
	public void validatePageNumberAndPageSize_When_PageNumberIs1AndPageSizeIsNull_Then_Succeeds() {
		PaginationValidator.validatePageNumberAndPageSize(1, null);
	}

	@Test
	public void validatePageNumberAndPageSize_When_PageNumberIsNullAndPageSizeIsNull_Then_Succeeds() {
		PaginationValidator.validatePageNumberAndPageSize(null, null);
	}

	@Test(expected = RestException.class)
	public void validatePageNumberAndPageSize_When_PageNumberIs1AndPageSizeIs0_Then_BadRequest() {
		try {
			PaginationValidator.validatePageNumberAndPageSize(1, 0);
		} catch (RestException e){
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("PageSize must be between 1 and 50 when present", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePageNumberAndPageSize_When_PageNumberIs0AndPageSizeIs1_Then_BadRequest() {
		try {
			PaginationValidator.validatePageNumberAndPageSize(0, 1);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("PageNumber must be greater than 0 when present", e.getDescription());
			throw e;
		}
	}

}