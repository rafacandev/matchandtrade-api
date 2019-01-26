package com.matchandtrade.persistence.criteria;

import com.matchandtrade.persistence.common.Criterion;
import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

import static com.matchandtrade.rest.service.SearchRecipeService.Field.ARTICLE_ID;
import static com.matchandtrade.rest.service.SearchRecipeService.Field.TRADE_ID;
import static com.matchandtrade.rest.service.SearchRecipeService.Field.USER_ID;
import static java.util.Collections.emptyList;

@Component
public class ArticleNativeQueryRepository {
	@Autowired
	private EntityManager entityManager;

	@SuppressWarnings(value = "unchecked")
	public SearchResult<ArticleEntity> search(SearchCriteria searchCriteria) {
		SqlBuilder sqlBuilder = new SqlBuilder(searchCriteria);

		Query queryContent = entityManager.createNativeQuery(sqlBuilder.buildSql(), ArticleEntity.class);
		Query queryCount = entityManager.createNativeQuery(sqlBuilder.buildCountSql());

		for (Criterion c : searchCriteria.getCriteria()) {
			queryCount.setParameter(c.getField().toString(), c.getValue());
			queryContent.setParameter(c.getField().toString(), c.getValue());
		}

		Pagination pagination = searchCriteria.getPagination();
		BigInteger count = (BigInteger) queryCount.getSingleResult();
		if (count.intValue() < 1) {
			return new SearchResult<>(emptyList(), pagination);
		}

		List<ArticleEntity> articles = queryContent.getResultList();
		return new SearchResult(articles, new Pagination(pagination.getNumber(), pagination.getSize(), count.longValue()));
	}

	private static class SqlBuilder {
		private List<Criterion> criteria;
		private final String pagination;

		public SqlBuilder(SearchCriteria searchCriteria) {
			this.criteria = searchCriteria.getCriteria();
			this.pagination =
				  " OFFSET "
				+ (searchCriteria.getPagination().getNumber() - 1) * searchCriteria.getPagination().getSize()
				+ " LIMIT " + searchCriteria.getPagination().getSize();
		}

		private void addWhereClause(StringBuilder currentWhere, String parameterClause, Criterion criterion) {
			if (currentWhere.length() == 0) {
				currentWhere.append(" WHERE ");
			} else {
				currentWhere
					.append(" ")
					.append(criterion.getLogicalOperator())
					.append(" ");
			}
			currentWhere
				.append(parameterClause)
				.append(criterion.getField().toString());
		}

		public String buildCountSql() {
			return buildSqlWithoutPagination().replace("SELECT *", "SELECT COUNT(*)");
		}

		public String buildSql() {
			return buildSqlWithoutPagination() + " ORDER BY article.article_id " + pagination;
		}

		private String buildSqlWithoutPagination() {
			StringBuilder sql = new StringBuilder("SELECT * FROM article");
			StringBuilder where = new StringBuilder();

			for (Criterion c : criteria) {
				if (ARTICLE_ID == c.getField()) {
					addWhereClause(where, "article.article_id = :", c);
				} if (TRADE_ID == c.getField()) {
					sql.append(" INNER JOIN membership_to_article m2a ON m2a.article_id = article.article_id");
					sql.append(" INNER JOIN membership ON membership.membership_id = m2a.membership_id");
					sql.append(" INNER JOIN trade ON trade.trade_id = membership.trade_id");
					addWhereClause(where, "trade.trade_id = :", c);
				} else if (USER_ID == c.getField()) {
					sql.append(" INNER JOIN user_to_article u2a ON u2a.article_id = article.article_id");
					addWhereClause(where, "u2a.user_id = :", c);
				}
			}
			return sql.toString() + where.toString();
		}
	}
}
