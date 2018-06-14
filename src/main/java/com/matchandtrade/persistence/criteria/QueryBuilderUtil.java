package com.matchandtrade.persistence.criteria;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.matchandtrade.persistence.common.Criterion;
import com.matchandtrade.persistence.common.Criterion.LogicalOperator;
import com.matchandtrade.persistence.common.Criterion.Restriction;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.Sort;

public class QueryBuilderUtil {

	// Utility classes should not have public constructors
	private QueryBuilderUtil() { }
	
	/**
	 * Builds a string representing a JPA Query WHERE clause.
	 * It takes in consideration the {@code Criterion.LogicalOperator} and {@code Criterion.Restriction} 
	 * @param field
	 * @param criterion
	 * @return
	 */
	private static String buildClauses(List<Criterion> criteria) {
		StringBuilder result = new StringBuilder();
		criteria.forEach( criterion -> {
			// Check if result already starts with WHERE
			if (result.lastIndexOf(" WHERE") < 0) {
				result.append(" WHERE");
				result.append(buildClause(criterion.getField().alias(), criterion.getField().name(), criterion, false));
			} else {
				result.append(buildClause(criterion.getField().alias(), criterion.getField().name(), criterion, true));
			}
		});
		return result.toString();
	}
	
	private static String buildClause(final String alias, final String param, Criterion criterion, boolean prependOperator) {
		StringBuilder result = new StringBuilder();
		
		if (prependOperator && criterion.getLogicalOperator().equals(LogicalOperator.AND)) {
			result.append(" AND");
		} else if (prependOperator && criterion.getLogicalOperator().equals(LogicalOperator.OR)) {
			result.append(" OR");
		}
		
		if (criterion.getRestriction().equals(Restriction.EQUALS)) {
			result.append(" " + alias + " = :" + param);
		} else if (criterion.getRestriction().equals(Restriction.NOT_EQUALS)) {
			result.append(" " + alias + " != :" + param);
		} else if (criterion.getRestriction().equals(Restriction.EQUALS_IGNORE_CASE)) {
			result.append(" UPPER(" + alias + ") = UPPER(:" + param + ")");
		} else if (criterion.getRestriction().equals(Restriction.LIKE_IGNORE_CASE)) {
			result.append(" UPPER(" + alias + ") LIKE UPPER(:" + param + ")");
		}
		
		return result.toString();
	}
	
	/**
	 * Build clauses for the given hql {@code buildClauses(hql)}.
	 * Creates a Query which is then parameterized with the given criteria. 
	 * 
	 * @param criteria
	 * @param hql to be used to buildClauses() and the create the Query
	 * @param entityManager to create the Query
	 * @return parameterized query for the given criteria
	 */
//	public static Query parameterizeQuery(List<Criterion> criteria, StringBuilder hql, EntityManager entityManager) {
//		hql.append(QueryBuilderUtil.buildClauses(criteria));
//		Query result = entityManager.createQuery(hql.toString());
//		criteria.forEach(c -> result.setParameter(c.getField().name(), c.getValue()));
//		return result;
//	}
	
	public static <T extends Sort> String parameterizeSort(List<T> list) {
		if (list.isEmpty()) {
			return "";
		}
		StringBuilder result = new StringBuilder(" ORDER BY ");
		list.forEach(sort -> {
			result.append(" " + sort.field().alias() + " " + sort.type());
		});
		return result.toString();
	}

	public static Query buildQuery(SearchCriteria searchCriteria, StringBuilder hql, EntityManager entityManager) {
		return buildQuery(searchCriteria, hql, entityManager, false);
	}

	private static String buildSort(List<Sort> sortList) {
		if (sortList.isEmpty()) {
			return "";
		}
		StringBuilder result = new StringBuilder(" ORDER BY ");
		sortList.forEach(sort -> {
			result.append(" " + sort.field().alias() + " " + sort.type());
		});
		return result.toString();
	}

	public static Query buildQuery(SearchCriteria searchCriteria, StringBuilder hql, EntityManager entityManager, boolean skipSorting) {
		hql.append(buildClauses(searchCriteria.getCriteria()));
		if (!skipSorting) {
			hql.append(buildSort(searchCriteria.getSortList()));
		}
		Query result = entityManager.createQuery(hql.toString());
		searchCriteria.getCriteria().forEach(c -> result.setParameter(c.getField().name(), c.getValue()));
		return result;
	}
}
