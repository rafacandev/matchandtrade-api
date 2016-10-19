package com.matchandtrade.persistence.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.common.util.PersistenceUtil;
import com.matchandtrade.persistence.entity.Entity;


/**
 * Generic Dao class to handle the most common operations performed by every Dao.
 * @author rafael.santos.bra@gmail.com
 */
public abstract class Dao<T extends Entity> {

    @Autowired
    private SessionFactory sessionFactory;

    protected abstract Criteria buildSearchCriteria(SearchCriteria searchCriteria);

    @SuppressWarnings("unchecked")
    @Transactional
    public T get(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return (T) session.get(getEntityClass(), id);
    }

    Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    @Transactional
	public SearchResult<T> search(SearchCriteria searchCriteria) {
		Criteria mainCriteria = buildSearchCriteria(searchCriteria);
		Criteria paginationCriteria = buildSearchCriteria(searchCriteria);
		// Get pagination from paginationCriteria
		Pagination resultPagination = PersistenceUtil.getPagination(searchCriteria.getPagination(), paginationCriteria);
		// Apply pagination parameters to the main criteria
		PersistenceUtil.applyPaginationToCriteria(resultPagination, mainCriteria);
		// Set Result Transformer
		mainCriteria.setResultTransformer(new AliasToBeanResultTransformer(getEntityClass()));
		// List results
		@SuppressWarnings("unchecked")
		List<T> resultList = mainCriteria.list();
		// Return results
		SearchResult<T> result = new SearchResult<>(resultList, resultPagination);
		return result;
	}

	/**
     * When extending this class, you need to implement <i>getEntityClass()</i>
     * and return the class used as generic for Dao.java
     *
     * @return Entity used as generic for this class
     */
    protected abstract Class<T> getEntityClass();

    @Transactional
    public void save(T entity) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(entity);
    }

}
