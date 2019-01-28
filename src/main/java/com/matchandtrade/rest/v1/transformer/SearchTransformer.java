package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.common.*;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.Entity;
import com.matchandtrade.rest.Json;
import com.matchandtrade.rest.service.SearchRecipeService;
import com.matchandtrade.rest.v1.json.search.*;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

public class SearchTransformer {

	private SearchTransformer() {}

	private static final ArticleTransformer articleTransformer = new ArticleTransformer();

	public static SearchResult<Json> transform(SearchResult<Entity> searchResult, Recipe recipe) {
		List<Json> resultList = searchResult.getResultList().stream()
			.map(entity -> transform(recipe, entity))
			.collect(Collectors.toList());
		return new SearchResult<>(resultList, searchResult.getPagination());
	}

	private static Json transform(Recipe recipe, Entity entity) {
		if (Recipe.ARTICLES == recipe) {
			ArticleEntity article = (ArticleEntity) entity;
			return articleTransformer.transform(article);
		} else {
			throw new InvalidParameterException("Unsupported recipe: " + recipe);
		}
	}

	private static Sort transform(SortJson sort) {
		Sort.Type type = Sort.Type.valueOf(sort.getType().name());
		return new Sort(transformField(sort.getField()), type);
	}

	public static SearchCriteria transform(SearchCriteriaJson searchCriteriaJson, Integer pageNumber, Integer pageSize) {
		SearchCriteria result = new SearchCriteria(new Pagination(pageNumber, pageSize));
		for (CriterionJson criterionJson : searchCriteriaJson.getCriteria()) {
			Criterion criterionEntity = new Criterion(
				transformField(criterionJson.getField()),
				criterionJson.getValue(),
				transformOperator(criterionJson.getOperator()),
				transformRestriction(criterionJson.getMatcher())
			);
			result.getCriteria().add(criterionEntity);
		}

		searchCriteriaJson.getSorts().forEach(sort -> {
			result.addSort(transform(sort));
		});
		return result;
	}

	public static Field transformField(String field) {
		for (Field f : SearchRecipeService.Field.values()) {
			if (f.alias().equals(field)) {
				return f;
			}
		}
		throw new IllegalArgumentException("Field not supported: " + field);
	}

	private static Criterion.Restriction transformRestriction(Matcher matcher) {
		return Criterion.Restriction.valueOf(matcher.name());
	}

	private static Criterion.LogicalOperator transformOperator(Operator operator) {
		return Criterion.LogicalOperator.valueOf(operator.name());
	}
}
