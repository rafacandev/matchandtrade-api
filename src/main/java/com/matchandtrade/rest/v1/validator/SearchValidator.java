package com.matchandtrade.rest.v1.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.search.SearchCriteriaJson;

@Component
public class SearchValidator {
	
	//Utility classes should not have public constructors
	SearchValidator() { }

	/**
	 * Validates if request.recipe is present.
	 * @param json
	 */
	public static void validatePost(SearchCriteriaJson requestBody, Integer pageNumber, Integer pageSize) {
		PaginationValidator.validatePageNumberAndPageSize(pageNumber, pageSize);
		
		if (requestBody.getRecipe() == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Recipe is mandatory.");
		}
	}

}
