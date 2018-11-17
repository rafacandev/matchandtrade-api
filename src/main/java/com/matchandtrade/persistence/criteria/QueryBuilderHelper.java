package com.matchandtrade.persistence.criteria;

import com.matchandtrade.persistence.common.Criterion;
import com.matchandtrade.persistence.common.Criterion.LogicalOperator;
import com.matchandtrade.persistence.common.Criterion.Restriction;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

@Component
public class QueryBuilderHelper {

	@Autowired
	private EntityManager entityManager;

	// Utility classes should not have public constructors
	private QueryBuilderHelper() { }
	
	/**
	 * Builds a string representing a JPA Query WHERE clause.
	 * It takes in consideration the {@code Criterion.LogicalOperator} and {@code Criterion.Restriction} 
	 */
	private static String buildClauses(List<Criterion> criteria) {
		Deque<Criterion> queue = new ArrayDeque<>(criteria);
		// The first criterion goes along with the WHERE clause and does not contain any operator
		StringBuilder result = new StringBuilder();
		if (!queue.isEmpty()) {
			result.append(" WHERE");
			Criterion criterion = queue.pop();
			result.append(" ").append(buildClause(criterion.getField().alias(), criterion.getField().toString(), criterion));
		}
		// All subsequent clauses contains operator
		for (Criterion criterion : queue) {
			result.append(" ").append(criterion.getLogicalOperator().name());
			result.append(" ").append(buildClause(criterion.getField().alias(), criterion.getField().toString(), criterion));
		}
		return result.toString();
	}
	
	private static String buildClause(final String alias, final String param, Criterion criterion) {
		StringBuilder result = new StringBuilder();
		if (criterion.getRestriction().equals(Restriction.EQUALS)) {
			result.append(alias).append(" = :").append(param);
		} else if (criterion.getRestriction().equals(Restriction.NOT_EQUALS)) {
			result.append(alias).append(" != :").append(param);
		} else if (criterion.getRestriction().equals(Restriction.EQUALS_IGNORE_CASE)) {
			result.append("UPPER(").append(alias).append(") = UPPER(:").append(param).append(")");
		} else if (criterion.getRestriction().equals(Restriction.LIKE_IGNORE_CASE)) {
			result.append("UPPER(").append(alias).append(") LIKE UPPER(:").append(param).append(")");
		}
		return result.toString();
	}

	public Query buildQuery(SearchCriteria searchCriteria, StringBuilder hql) {
		return buildQuery(searchCriteria, hql, false);
	}

	private static String buildSort(List<Sort> sortList) {
		if (sortList.isEmpty()) {
			return "";
		}
		StringBuilder result = new StringBuilder(" ORDER BY ");
		for(Sort sort : sortList) {
			result.append(" ").append(sort.field().alias()).append(" ").append(sort.type());
		}
		return result.toString();
	}

	public Query buildQuery(SearchCriteria searchCriteria, StringBuilder hql, boolean skipSorting) {
		hql.append(buildClauses(searchCriteria.getCriteria()));
		if (!skipSorting) {
			hql.append(buildSort(searchCriteria.getSortList()));
		}
		Query result = entityManager.createQuery(hql.toString());
		for (Criterion c : searchCriteria.getCriteria()) {
			result.setParameter(c.getField().toString(), c.getValue());
		}
		return result;
	}

}
