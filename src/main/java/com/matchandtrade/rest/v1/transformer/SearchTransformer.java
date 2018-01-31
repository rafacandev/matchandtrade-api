package com.matchandtrade.rest.v1.transformer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ItemRecipeQueryBuilder;
import com.matchandtrade.persistence.dto.ItemAndTradeMembershipIdDto;
import com.matchandtrade.persistence.entity.Entity;
import com.matchandtrade.rest.Json;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.search.Recipe;
import com.matchandtrade.rest.v1.json.search.SearchCriteriaJson;

public class SearchTransformer {

	private SearchTransformer() {}

	public static SearchCriteria transform(SearchCriteriaJson request) {
		SearchCriteria result = new SearchCriteria(new Pagination(1, 10));
		if (Recipe.ITEMS == request.getRecipe()) {
			request.getCriteria().forEach(entry -> {
				if ("trade.tradeId".equals(entry.getKey())) {
					result.addCriterion(ItemRecipeQueryBuilder.Field.TRADE_ID, entry.getValue());
				}
			});
		}
		return result;
	}
	
	public static SearchResult<Json> transform(SearchResult<Entity> searchResult, Recipe recipe) {
		List<Json> resultList = searchResult.getResultList().stream().map(v -> {
			if (Recipe.ITEMS == recipe) {
				ItemAndTradeMembershipIdDto tradeMembershipAndItemDto = (ItemAndTradeMembershipIdDto) v;
				return ItemTransformer.transform(tradeMembershipAndItemDto.getItem());
			} else {
				throw new RestException(HttpStatus.BAD_REQUEST, "Unsupported recipe: " + recipe);
			}
		})
		.filter(Objects::nonNull)
		.collect(Collectors.toList());
		return new SearchResult<>(resultList, searchResult.getPagination());
	}

}
