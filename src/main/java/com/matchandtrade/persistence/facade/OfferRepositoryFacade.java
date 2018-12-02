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
		offerRepository.deleteById(offerId);
	}

	// TODO: Optional ??
	public OfferEntity findByOfferId(Integer offerId) {
		return offerRepository.findById(offerId).get();
	}

	public List<OfferEntity> findByOfferedArticleId(Integer offeredArticleId) {
		return offerRepository.findByOfferedArticleArticleId(offeredArticleId);
	}

	public void save(OfferEntity entity) {
		offerRepository.save(entity);
	}
}
