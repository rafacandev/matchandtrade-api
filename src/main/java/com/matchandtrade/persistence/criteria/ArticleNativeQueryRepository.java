package com.matchandtrade.persistence.criteria;

import com.matchandtrade.persistence.common.*;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.rest.service.SearchRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

import static com.matchandtrade.rest.service.SearchRecipeService.Field.*;
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
		private List<Sort> sortList;
		private List<Criterion> criteria;
		private final String pagination;

		public SqlBuilder(SearchCriteria searchCriteria) {
			this.sortList = searchCriteria.getSorts();
			this.criteria = searchCriteria.getCriteria();
			this.pagination =
				  " OFFSET "
				+ (searchCriteria.getPagination().getNumber() - 1) * searchCriteria.getPagination().getSize()
				+ " LIMIT " + searchCriteria.getPagination().getSize();
		}

		private void addWhereClause(StringBuilder currentWhere, SearchRecipeService.Field field, Criterion criterion) {
			if (currentWhere.length() == 0) {
				currentWhere.append(" WHERE ");
			} else {
				currentWhere
					.append(" ")
					.append(criterion.getLogicalOperator())
					.append(" ");
			}
			currentWhere
				.append(toFieldAlias(field))
				.append(" = :")
				.append(criterion.getField().toString());
		}

		public String buildCountSql() {
			return buildSqlWithoutPagination().replace("SELECT article.*", "SELECT COUNT(*)");
		}

		public String buildSql() {
			return buildSqlWithoutPagination() + buildOrderBy() + pagination;
		}

		public String buildOrderBy() {
			if (this.sortList.isEmpty()) {
				return " ORDER BY article.article_id ";
			}
			StringBuffer result = new StringBuffer();
			this.sortList.forEach(sort -> {
				result
					.append(" ORDER BY UPPER(")
					.append(toFieldAlias(sort.field()))
					.append(") ")
					.append(sort.type().name());
			});
			return result.toString();
		}

		private String buildSqlWithoutPagination() {
			StringBuilder sql = new StringBuilder("SELECT article.* FROM article");
			StringBuilder where = new StringBuilder();

			for (Criterion c : criteria) {
				SearchRecipeService.Field field = (SearchRecipeService.Field) c.getField();
				if (ARTICLE_ID == field || ARTICLE_NAME == field) {
					addWhereClause(where, field, c);
				} if (TRADE_ID == c.getField()) {
					sql.append(" INNER JOIN membership_to_article m2a ON m2a.article_id = article.article_id");
					sql.append(" INNER JOIN membership ON membership.membership_id = m2a.membership_id");
					sql.append(" INNER JOIN trade ON trade.trade_id = membership.trade_id");
					addWhereClause(where, TRADE_ID, c);
				} else if (USER_ID == c.getField()) {
					sql.append(" INNER JOIN user_to_article u2a ON u2a.article_id = article.article_id");
					addWhereClause(where, USER_ID, c);
				}
			}
			return sql.toString() + where.toString();
		}

		private String toFieldAlias(Field field) {
			if (field.toString().equals(ARTICLE_ID.toString())) {
				return "article.article_id";
			} else if (field.toString().equals(ARTICLE_NAME.toString())) {
				return "article.name";
			}else if (field.toString().equals(USER_ID.toString())) {
				return "u2a.user_id";
			} else if (field.toString().equals(TRADE_ID.toString())) {
				return "trade.trade_id";
			} else {
				return "";
			}
		}
	}
}
