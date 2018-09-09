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
	
	public List<OfferEntity> findOffersByMembership(Integer membershipId) {
		Query query = entityManager.createQuery("SELECT offer FROM MembershipEntity membership JOIN membership.offers AS offer WHERE membership.membershipId=:membershipId");
		query.setParameter("membershipId", membershipId);
		return query.getResultList();
	}

}
