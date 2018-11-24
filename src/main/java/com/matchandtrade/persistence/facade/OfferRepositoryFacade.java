package com.matchandtrade.persistence.facade;

import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.repository.OfferRepository;
import com.matchandtrade.persistence.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OfferRepositoryFacade {
	
	@Autowired
	private OfferRepository offerRepository;
	
	@Autowired
	private TradeRepository tradeRepository;
	
	public void delete(Integer offerId) {
		offerRepository.delete(offerId);
	}

	public OfferEntity find(Integer offerId) {
		return offerRepository.findOne(offerId);
	}

	public List<OfferEntity> findByOfferedArticleId(Integer offeredArticleId) {
		return offerRepository.findByOfferedArticleArticleId(offeredArticleId);
	}

	public void save(OfferEntity entity) {
		offerRepository.save(entity);
	}
}
