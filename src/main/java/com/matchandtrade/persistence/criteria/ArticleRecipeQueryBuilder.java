package com.matchandtrade.persistence.criteria;

import com.matchandtrade.persistence.common.Criterion;
import com.matchandtrade.persistence.common.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Query;

import static com.matchandtrade.persistence.criteria.ArticleRecipeQueryBuilder.Field.ARTICLE_ID;
import static com.matchandtrade.persistence.criteria.ArticleRecipeQueryBuilder.Field.TRADE_ID;
import static com.matchandtrade.persistence.criteria.ArticleRecipeQueryBuilder.Field.USER_ID;

@Component
public class ArticleRecipeQueryBuilder implements QueryBuilder {

	@Autowired
	private QueryBuilderHelper queryBuilderHelper;

	public enum Field implements com.matchandtrade.persistence.common.Field {
		ARTICLE_ID("article.articleId"),
		TRADE_ID("membership.trade.tradeId"),
		USER_ID("user.userId");

		private String alias;

		Field(String alias) {
			this.alias = alias;
		}

		@Override
		public String alias() {
			return alias;
		}
	}


	@Override
	public Query buildCountQuery(SearchCriteria searchCriteria) {
		StringBuilder hql = new StringBuilder("SELECT COUNT(*) " + buildBasicHql(searchCriteria));
		return queryBuilderHelper.buildQuery(searchCriteria, hql, true);
	}

	private String buildBasicHql(SearchCriteria searchCriteria) {
		StringBuilder result = new StringBuilder("FROM  ArticleEntity AS article,");

		boolean searchesForTradeId = searchCriteria.getCriteria().stream().anyMatch(c -> c.getField().alias().equals(TRADE_ID.alias()));
		if (searchesForTradeId) {
			result.append(" MembershipEntity AS membership WHERE membership.articles.articleId=article.articleId");
		}

		boolean searchesForUserId = searchCriteria.getCriteria().stream().anyMatch(c -> c.getField().alias().equals(USER_ID.alias()));
		if (searchesForUserId) {
			result.append(" INNER JOIN UserEntity AS user");
		}

		return result.toString();
	}

	@Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
		StringBuilder hql = new StringBuilder("SELECT article " + buildBasicHql(searchCriteria));
		return queryBuilderHelper.buildQuery(searchCriteria, hql);
	}
}
