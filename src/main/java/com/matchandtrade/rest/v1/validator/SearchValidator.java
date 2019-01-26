package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.search.Recipe;
import com.matchandtrade.rest.v1.json.search.SearchCriteriaJson;
import com.matchandtrade.rest.v1.transformer.SearchTransformer;
import org.springframework.http.HttpStatus;

public class SearchValidator {
	public static void validatePost(SearchCriteriaJson request, Integer pageNumber, Integer pageSize) {
		PaginationValidator.validatePageNumberAndPageSize(pageNumber, pageSize);

		if (request.getRecipe() == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Recipe is mandatory");
		}
		try {
			Recipe.valueOf(request.getRecipe());
		} catch(IllegalArgumentException e) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Invalid recipe");
		}
		if (request.getCriteria().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Criteria is mandatory");
		}

		request.getCriteria().forEach(c -> {
			try {
				SearchTransformer.transformField(c.getField());
			} catch (IllegalArgumentException e) {
				throw new RestException(HttpStatus.BAD_REQUEST, "Invalid field: " + c.getField());
			}
		});
	}
}
