package com.matchandtrade.persistence.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.persistence.entity.UserEntity;

@Component
public class UserDao extends Dao<UserEntity> {

	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional
	public UserEntity get(String email) {
		// TODO use buildSearchCriteria() instead
		Session session = sessionFactory.getCurrentSession();
		Criteria c = session.createCriteria(UserEntity.class);
		c.add(Restrictions.eq("email", email));
		UserEntity result = (UserEntity) c.uniqueResult();
		return result;
	}

	@Override
	protected Class<UserEntity> getEntityClass() {
		return UserEntity.class;
	}

	@Override
	protected Criteria buildSearchCriteria(SearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
