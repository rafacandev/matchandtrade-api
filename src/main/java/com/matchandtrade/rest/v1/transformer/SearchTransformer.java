package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.common.*;
import com.matchandtrade.persistence.criteria.ArticleNativeQueryRepository;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.Entity;
import com.matchandtrade.rest.Json;
import com.matchandtrade.rest.v1.json.search.Matcher;
import com.matchandtrade.rest.v1.json.search.Operator;
import com.matchandtrade.rest.v1.json.search.Recipe;
import com.matchandtrade.rest.v1.json.search.SearchCriteriaJson;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

public class SearchTransformer {

	private SearchTransformer() {}

	private static final ArticleTransformer articleTransformer = new ArticleTransformer();

	public static SearchResult<Json> transform(SearchResult<Entity> searchResult, Recipe recipe) {
		List<Json> resultList = searchResult.getResultList().stream()
			.map(entity -> {
				if (Recipe.ARTICLES == recipe) {
					ArticleEntity article = (ArticleEntity) entity;
					return articleTransformer.transform(article);
				} else {
					throw new InvalidParameterException("Unsupported recipe: " + recipe);
				}
			})
			.collect(Collectors.toList());
		return new SearchResult<>(resultList, searchResult.getPagination());
	}

	public static SearchCriteria transform(SearchCriteriaJson searchCriteriaJson, Integer pageNumber, Integer pageSize) {
		SearchCriteria result = new SearchCriteria(new Pagination(pageNumber, pageSize));
		for (com.matchandtrade.rest.v1.json.search.Criterion criterionJson : searchCriteriaJson.getCriteria()) {
			Criterion criterion = new Criterion(
				transformField(criterionJson.getKey()),
				criterionJson.getValue(),
				transformOperator(criterionJson.getOperator()),
				transformRestriction(criterionJson.getMatcher()));
			result.getCriteria().add(criterion);
		}
		return result;
	}

	private static Criterion.Restriction transformRestriction(Matcher matcher) {
		return Criterion.Restriction.valueOf(matcher.name());
	}

	private static Criterion.LogicalOperator transformOperator(Operator operator) {
		return Criterion.LogicalOperator.valueOf(operator.name());
	}

	private static Field transformField(String key) {
		switch (key) {
			case "Trade.tradeId":
				return ArticleNativeQueryRepository.Field.TRADE_ID;
			default:
				throw new IllegalArgumentException("Invalid key: " + key);
		}
	}
}
