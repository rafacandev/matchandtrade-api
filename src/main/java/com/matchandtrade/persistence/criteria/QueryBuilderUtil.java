package com.matchandtrade.persistence.criteria;

import com.matchandtrade.common.Criterion;
import com.matchandtrade.common.Criterion.LogicalOperator;
import com.matchandtrade.common.Criterion.Restriction;

public class QueryBuilderUtil {

	// TODO
	private QueryBuilderUtil() { }
	
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
		}
		return result.toString();
	}
}
