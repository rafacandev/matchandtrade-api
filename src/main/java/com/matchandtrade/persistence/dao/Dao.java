package com.matchandtrade.persistence.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.entity.Entity;


/**
 * Generic DAO class to handle the most common operations performed by every <code>Dao</code>.
 * @author rafael.santos.bra@gmail.com
 */
public class Dao<T extends Entity> {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Returns the <code>Entity</code> for the given <code>id</code>.
     * @param id
     * @return <code>Entity</code> for the given <code>id</code>
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
     * Deletes <code>entity</code>.
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
     * Saves or updates the <code>entity</code>.
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
