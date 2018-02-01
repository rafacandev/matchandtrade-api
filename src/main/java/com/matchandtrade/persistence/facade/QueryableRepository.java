package com.matchandtrade.persistence.facade;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.QueryBuilder;
import com.matchandtrade.persistence.entity.Entity;

/**
 * Generic repository class with ability to query entities.
 * 
 * @author rafael.santos.bra@gmail.com
 */
@Repository
public class QueryableRepository<T extends Entity> {
	
    /**
	 * Apply pagination value to the criteria
	 * 
	 * @param pagination
	 * @param query
	 */
	private static void applyPaginationToCriteria(Pagination pagination, Query query) {
		int firstResult = pagination.getSize() * (pagination.getNumber() -1);
		query.setFirstResult(firstResult);
		query.setMaxResults(pagination.getSize());
	}

	/**
	 * Queries the database for a matching {@code searchCriteria} applying the {@code searchCriteria.getPagination()}
	 * @param searchCriteria to be used
	 * @param queryBuilder to parse searchCriteria in org.hibernate.Criteria
	 * @return
	 */
	public SearchResult<T> query(SearchCriteria searchCriteria, QueryBuilder queryBuilder) {
		return query(searchCriteria, queryBuilder, null);
	}

	/**
	 * Queries the database for a matching {@code searchCriteria} applying the {@code searchCriteria.getPagination()}
	 * @param searchCriteria to be used
	 * @param queryBuilder to parse searchCriteria in org.hibernate.Criteria
	 * @param resultTransformer if not null, the result will be transformed using the instance of 'resultTrasnformer'
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public SearchResult<T> query(SearchCriteria searchCriteria, QueryBuilder queryBuilder, ResultTransformer resultTransformer) {
		// Firstly, count how many records the searchCriteria returns. Important for pagination purposes
		Query countQuery = queryBuilder.buildCountQuery(searchCriteria);
		// Set pagination total
		Long rowCount = (Long) countQuery.getSingleResult();
		searchCriteria.getPagination().setTotal(rowCount);
		// Secondly, query the database with the proper pagination.
		// Do not reuse countQuery since rowCount() modifies the query (I wish hibernate provided a way to clone Queries)...
		Query queryCriteria = queryBuilder.buildSearchQuery(searchCriteria);
		// Apply pagination parameters to the main criteria
		applyPaginationToCriteria(searchCriteria.getPagination(), queryCriteria);
		// Apply resultTransformer if exists
		if (resultTransformer != null) {
			queryCriteria.unwrap(org.hibernate.Query.class).setResultTransformer(resultTransformer);
		}
		// List results
		List<T> resultList = queryCriteria.getResultList();
		return new SearchResult<>(resultList, searchCriteria.getPagination());
	}

}
