package com.matchandtrade.rest.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matchandtrade.persistence.entity.OfferEntity;

/**
 * Helper class to facilitate queries used on tests.
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
@Service
@SuppressWarnings("unchecked")
public class TestQuery {

	@Autowired
	private EntityManager entityManager;
	
	public List<OfferEntity> findOffersByTradeMembership(Integer tradeMembershipId) {
		Query query = entityManager.createQuery("SELECT offer FROM TradeMembershipEntity membership JOIN membership.offers AS offer WHERE membership.tradeMembershipId=:tradeMembershipId");
		query.setParameter("tradeMembershipId", tradeMembershipId);
		return query.getResultList();
	}

}
