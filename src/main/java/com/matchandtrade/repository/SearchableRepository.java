package com.matchandtrade.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.RootEntityResultTransformer;
import org.springframework.stereotype.Repository;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.criteria.CriteriaBuilder;
import com.matchandtrade.persistence.entity.Entity;

/**
 * Generic repository class with ability to search for entities.
 * 
 * @author rafael.santos.bra@gmail.com
 */
@Repository
public class SearchableRepository<T extends Entity> {

	/**
	 * Apply pagination value to the criteria
	 * 
	 * @param pagination
	 * @param criteria
	 */
	private static void applyPaginationToCriteria(Pagination pagination, Criteria criteria) {
		int firstResult = pagination.getSize() * (pagination.getNumber() -1);
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(pagination.getSize());
	}

	/**
	 * Counts the result for the {@code criteria}.
	 * Keep in mind that it will apply projections to determine the {@code rowCount}.
	 * Finally, it will 
	 * <pre>
		criteria.setProjection(null);
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
	 * </pre>
	 * @param pagination
	 * @param criteria
	 * @return Pagination loaded from the persistence layer
	 */
	private static long rowCount(Criteria criteria) {
    	criteria.setProjection(Projections.rowCount());
		Long result = (Long) criteria.uniqueResult();
		criteria.setProjection(null);
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		if (result == null) {
			return 0;
		} else {
			return result.longValue();
		}
	}
    
	/**
	 * Searches the database for a matching {@code searchCriteria} applying the {@code searchCriteria.getPagination()}
	 * @param searchCriteria to be used
	 * @param criteriaBuilder CriteriaBuilder to parse searchCritia in org.hibernate.Criteria
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SearchResult<T> search(SearchCriteria searchCriteria, CriteriaBuilder criteriaBuilder) {
		// Firstly, count how many records the searchCriteria returns. Important for pagination purposes
		Criteria countCriteria = criteriaBuilder.buildSearchCriteria(searchCriteria);
		// Set pagination total
		long rowCount = rowCount(countCriteria);
		searchCriteria.getPagination().setTotal(rowCount);
		
		// Secondly, query the database with the proper pagination.
		// Do not reuse countCriteria since rowCount() modifies the criteria (I wish hibernate provided a why to clone Criteria)...
		Criteria queryCriteria = criteriaBuilder.buildSearchCriteria(searchCriteria);
		// Apply pagination parameters to the main criteria
		applyPaginationToCriteria(searchCriteria.getPagination(), queryCriteria);
		// Set Result Transformer
		queryCriteria.setResultTransformer(RootEntityResultTransformer.INSTANCE);
		// List results
		List<T> resultList = queryCriteria.list();
		return new SearchResult<>(resultList, searchCriteria.getPagination());
	}

}
