package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.common.*;
import com.matchandtrade.persistence.criteria.ArticleRecipeQueryBuilder;
import com.matchandtrade.persistence.dto.ArticleAndMembershipIdDto;
import com.matchandtrade.persistence.dto.Dto;
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

	public static SearchResult<Json> transform(SearchResult<Dto> searchResult, Recipe recipe) {
		List<Json> resultList = searchResult.getResultList().stream()
			.map(dto -> {
				if (Recipe.ARTICLES == recipe) {
					ArticleAndMembershipIdDto membershipAndArticleDto = (ArticleAndMembershipIdDto) dto;
					return articleTransformer.transform(membershipAndArticleDto.getArticle());
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
				return ArticleRecipeQueryBuilder.Field.TRADE_ID;
			default:
				throw new IllegalArgumentException("Invalid key: " + key);
		}
	}

}
