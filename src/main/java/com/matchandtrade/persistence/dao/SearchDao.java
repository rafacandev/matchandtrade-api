package com.matchandtrade.persistence.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.TupleSubsetResultTransformer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.persistence.entity.Entity;


/**
 * Generic DAO class with ability to search entities.
 * @author rafael.santos.bra@gmail.com
 */
@Component
public class SearchDao<T extends Entity> {
	
	/**
	 * Searches the database matching the <i>criteria</i> applying the <i>pagination</i> and transforming the results
	 * according with the <i>resultTransformer</i>
	 * @param criteria
	 * @param pagination
	 * @param resultTransformer
	 * @return paginated results for the <i>criteria</i>
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<T> search(Criteria criteria, Pagination pagination, TupleSubsetResultTransformer resultTransformer) {
		// Apply pagination parameters to the main criteria
		applyPaginationToCriteria(pagination, criteria);
		// Set Result Transformer
		criteria.setResultTransformer(resultTransformer);
		// List results
		return criteria.list();
	}
    
	/**
	 * Counts the result for the <i>criteria</i>.
	 * Keep in mind that it will apply projections to determine the <i>rowCount</i>.
	 * Finally, it will 
	 * <pre>
		criteria.setProjection(null);
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
	 * </pre>
	 * @param pagination
	 * @param criteria
	 * @return Pagination loaded from the persistence layer
	 */
    @Transactional
	public static long rowCount(Criteria criteria) {
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
    
	private static void applyPaginationToCriteria(Pagination pagination, Criteria criteria) {
		int firstResult = pagination.getSize() * (pagination.getNumber() -1);
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(pagination.getSize());
	}

}
