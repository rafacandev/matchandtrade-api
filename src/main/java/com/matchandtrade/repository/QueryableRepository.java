package com.matchandtrade.repository;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.RootEntityResultTransformer;
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
	 * Searches the database for a matching {@code searchCriteria} applying the {@code searchCriteria.getPagination()}
	 * @param searchCriteria to be used
	 * @param criteriaBuilder CriteriaBuilder to parse searchCritia in org.hibernate.Criteria
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SearchResult<T> query(SearchCriteria searchCriteria, QueryBuilder queryBuilder) {
		// Firstly, count how many records the searchCriteria returns. Important for pagination purposes
		Query countQuery = queryBuilder.buildCountQuery(searchCriteria);
		// Set pagination total
		Long rowCount = (Long) countQuery.uniqueResult();
		searchCriteria.getPagination().setTotal(rowCount);
		
		// Secondly, query the database with the proper pagination.
		// Do not reuse countCriteria since rowCount() modifies the criteria (I wish hibernate provided a why to clone Criteria)...
		Query queryCriteria = queryBuilder.buildSearchQuery(searchCriteria);
		// Apply pagination parameters to the main criteria
		applyPaginationToCriteria(searchCriteria.getPagination(), queryCriteria);
		// Set Result Transformer
		queryCriteria.setResultTransformer(RootEntityResultTransformer.INSTANCE);
		// List results
		List<T> resultList = queryCriteria.list();
		return new SearchResult<>(resultList, searchCriteria.getPagination());
	}

}
