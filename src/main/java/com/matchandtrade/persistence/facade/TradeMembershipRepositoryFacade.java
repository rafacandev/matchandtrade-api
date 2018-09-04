package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.repository.TradeMembershipRepository;

@Repository
public class TradeMembershipRepositoryFacade {

	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;

	public void delete(Integer tradeMembershipId) {
		tradeMembershipRepository.delete(tradeMembershipId);
	}

	public TradeMembershipEntity get(Integer tradeMembershipId) {
		return tradeMembershipRepository.findOne(tradeMembershipId);
	}

	public TradeMembershipEntity getByOfferId(Integer offerId) {
		return tradeMembershipRepository.findByOffers_ArticleId(offerId);
	}

	public void save(TradeMembershipEntity entity) {
		tradeMembershipRepository.save(entity);
	}
	
}
