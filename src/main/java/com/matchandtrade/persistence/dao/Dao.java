package com.matchandtrade.persistence.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.entity.Entity;


/**
 * Generic DAO class to handle the most common operations performed by every <i>Dao</i>.
 * @author rafael.santos.bra@gmail.com
 */
public abstract class Dao<T extends Entity> {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Returns the <i>Entity</i> for the given <i>id</i>.
     * @param id
     * @return <i>Entity</i> for the given <i>id</i>
     */
    @Transactional
    public T get(Class<T> clazz, Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return (T) session.get(clazz, id);
    }

    /**
     * Returns the current session.
     * @return
     */
    Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Saves or updates the <i>entity</i>.
     * @param entity
     */
    @Transactional
    public void save(T entity) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(entity);
    }

}
