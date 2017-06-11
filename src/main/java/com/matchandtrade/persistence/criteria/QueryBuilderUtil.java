package com.matchandtrade.persistence.criteria;

import javax.persistence.Query;

import com.matchandtrade.persistence.common.Criterion;
import com.matchandtrade.persistence.common.Criterion.LogicalOperator;
import com.matchandtrade.persistence.common.Criterion.Restriction;
import com.matchandtrade.persistence.common.Field;
import com.matchandtrade.persistence.common.SearchCriteria;

public class QueryBuilderUtil {

	// Utility classes should not have public constructors
	private QueryBuilderUtil() { }
	
	public static String buildClause(Field field, Criterion criterion) {
		return buildClause(field.alias(), field.name(), criterion);
	}
	
	public static String buildClause(final String alias, final String param, Criterion c) {
		StringBuilder result = new StringBuilder();
		
		if (c.getLogicalOperator().equals(LogicalOperator.AND)) {
			result.append(" AND");
		} else if (c.getLogicalOperator().equals(LogicalOperator.OR)) {
			result.append(" OR");
		}
		
		if (c.getRestriction().equals(Restriction.EQUALS)) {
			result.append(" " + alias + " = :" + param);
		} else if (c.getRestriction().equals(Restriction.NOT_EQUALS)) {
			result.append(" " + alias + " != :" + param);
		} else if (c.getRestriction().equals(Restriction.EQUALS_IGNORE_CASE)) {
			result.append(" UPPER(" + alias + ") = UPPER(:" + param + ")");
		} else if (c.getRestriction().equals(Restriction.LIKE_IGNORE_CASE)) {
			result.append(" UPPER(" + alias + ") LIKE UPPER(:" + param + ")");
		}
		return result.toString();
	}

	public static String buildClause(Criterion c) {
		return buildClause(c, true);
	}

	public static String buildClause(Criterion c, boolean needsLogicalOperator) {
		StringBuilder result = new StringBuilder();
		if (needsLogicalOperator) {
			if (c.getLogicalOperator().equals(LogicalOperator.AND)) {
				result.append(" AND");
			} else if (c.getLogicalOperator().equals(LogicalOperator.OR)) {
				result.append(" OR");
			}
		}

		String restriction = "=";
		if (c.getRestriction().equals(Restriction.NOT_EQUALS)) {
			restriction = "!=";
		}
		result.append(" " + c.getField().toString() + " " + restriction + " :" + c.getField().toString());
		return result.toString();
	}
	
	public static String buildClauses(SearchCriteria searchCriteria) {
		StringBuilder result = new StringBuilder();
		boolean needLogicalOperator = false;
		for (Criterion c : searchCriteria.getCriteria()) {
			result.append(buildClause(c, needLogicalOperator));
			needLogicalOperator = true;
		}
		return result.toString();
	}

	public static void setParameters(SearchCriteria searchCriteria, Query query) {
		for (Criterion c : searchCriteria.getCriteria()) {
			query.setParameter(c.getField().toString(), c.getValue());
		}
	}
}
