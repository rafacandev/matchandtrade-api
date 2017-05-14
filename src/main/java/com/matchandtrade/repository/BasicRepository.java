package com.matchandtrade.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.entity.Entity;


/**
 * Basic generic repository class to handle the most basic operations: get, delete, save.
 * 
 * @author rafael.santos.bra@gmail.com
 */
@Repository
public class BasicRepository<T extends Entity> {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Returns the {@code Entity} for the given {@code id}.
     * @param id
     * @return {@code Entity} for the given {@code id}
     */
    @Transactional
    public T get(Class<T> clazz, Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(clazz, id);
    }

    /**
     * Returns the current session.
     * @return
     */
    Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    /**
     * Deletes an {@code Entity}.
     * @param entity
     */
    @Transactional
    public void delete(T entity) {
    	if (entity != null) {
    		Session session = sessionFactory.getCurrentSession();
    		session.delete(entity);
		}
    }

    /**
     * Saves or updates the {@code Entity}.
     * @param entity
     */
    @Transactional
    public void save(T entity) {
    	if (entity != null) {
    		Session session = sessionFactory.getCurrentSession();
    		session.saveOrUpdate(entity);
		}
    }

}
