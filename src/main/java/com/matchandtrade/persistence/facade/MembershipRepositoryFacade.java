package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.repository.MembershipRepository;

@Repository
public class MembershipRepositoryFacade {

	@Autowired
	private MembershipRepository membershipRepository;

	public void delete(Integer membershipId) {
		membershipRepository.delete(membershipId);
	}

	public MembershipEntity find(Integer membershipId) {
		return membershipRepository.findOne(membershipId);
	}

	public MembershipEntity findByOfferId(Integer offerId) {
		return membershipRepository.findByOffers_OfferId(offerId);
	}

	public void save(MembershipEntity entity) {
		membershipRepository.save(entity);
	}
	
}
