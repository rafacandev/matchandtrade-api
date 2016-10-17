package com.matchandtrade.common.util;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import com.matchandtrade.common.Pagination;

public class PersistenceUtil {

	public static void applyPaginationToCriteria(Pagination pagination, Criteria criteria) {
		if (pagination.getLimit() == null || pagination.getPage() == null) {
			return;
		}
		int firstResult = pagination.getLimit() * (pagination.getPage() - 1);
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(pagination.getLimit());
	}
	
	/**
	 * Returns a new Pagination object based on Pagination and Criteria.
	 * Additionally, it will apply projections to determine the <i>rowCount</i>
	 * which will be set to <i>Pagination.total</i>. Therefore, do not use this
	 * method if you need to preserve any projections already assigned to the
	 * <i>Criteria</i> passed as parameter.
	 * 
	 * @param pagination
	 * @param criteria
	 * @return Pagination loaded from the persistence layer
	 */
	public static Pagination getPagination(Pagination pagination, Criteria criteria) {
		criteria.setProjection(Projections.rowCount());
		Long resultCount = (Long) criteria.uniqueResult();
		Pagination resultPagination = new Pagination(pagination.getPage(), pagination.getLimit(), resultCount);
		return resultPagination;
	}

}
