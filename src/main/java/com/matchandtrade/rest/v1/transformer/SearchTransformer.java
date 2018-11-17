package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.common.*;
import com.matchandtrade.persistence.common.Criterion.LogicalOperator;
import com.matchandtrade.persistence.criteria.ArticleRecipeQueryBuilder;
import com.matchandtrade.persistence.dto.ArticleAndMembershipIdDto;
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

	private static ArticleTransformer articleTransformer = new ArticleTransformer();

	public static SearchResult<Json> transform(SearchResult<Entity> searchResult, Recipe recipe) {
		List<Json> resultList = searchResult.getResultList().stream()
			.map(entity -> {
				if (Recipe.ARTICLES == recipe) {
					ArticleAndMembershipIdDto membershipAndArticleDto = (ArticleAndMembershipIdDto) entity;
					return articleTransformer.transform(membershipAndArticleDto.getArticle());
				} else {
					throw new InvalidParameterException("Unsupported recipe: " + recipe);
				}
			})
			.collect(Collectors.toList());
		return new SearchResult<>(resultList, searchResult.getPagination());
	}

	public static SearchCriteria transform(SearchCriteriaJson request, Pagination pagination) {
		if (Recipe.ARTICLES == request.getRecipe()) {
			return transformArticlesRecipe(request, pagination);
		} else {
			throw new InvalidParameterException("Unable to transform SearchCriteria with recipe: " + request.getRecipe());
		}
	}

	private static SearchCriteria transformArticlesRecipe(SearchCriteriaJson request, Pagination pagination) {
		SearchCriteria result = new SearchCriteria(pagination);
		request.getCriteria().forEach(entry -> {
			if ("Trade.tradeId".equals(entry.getKey())) {
				result.getCriteria().add(transformCriterion(ArticleRecipeQueryBuilder.Field.TRADE_ID, entry.getValue(), entry.getOperator(), entry.getMatcher()));
			}
			if ("Membership.membershipId".equals(entry.getKey())) {
				result.getCriteria().add(transformCriterion(ArticleRecipeQueryBuilder.Field.TRADE_MEMBERSHIP_ID, entry.getValue(), entry.getOperator(), entry.getMatcher()));
			}
		});
		return result;
	}
	
	private static Criterion transformCriterion(Field field, Object value, Operator operator, Matcher matcher) {
		// Transform operator
		LogicalOperator persistenceOperator = LogicalOperator.AND;
		if (operator == Operator.OR) {
			persistenceOperator = LogicalOperator.OR;
		}
		
		// Transform matcher
		Criterion.Restriction persistanceRestriction; 
		switch (matcher) {
		case NOT_EQUALS:
			persistanceRestriction = Criterion.Restriction.NOT_EQUALS;
			break;
		case EQUALS_IGNORE_CASE:
			persistanceRestriction = Criterion.Restriction.EQUALS_IGNORE_CASE;
			break;
		case LIKE_IGNORE_CASE:
			persistanceRestriction = Criterion.Restriction.LIKE_IGNORE_CASE;
			break;
		default:
			persistanceRestriction = Criterion.Restriction.EQUALS;
			break;
		}
		
		return new Criterion(field, value, persistenceOperator, persistanceRestriction);
	}

}
