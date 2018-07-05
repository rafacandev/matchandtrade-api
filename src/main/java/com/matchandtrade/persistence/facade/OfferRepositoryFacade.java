package com.matchandtrade.persistence.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.common.PersistenceUtil;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.repository.OfferRepository;
import com.matchandtrade.persistence.repository.TradeRepository;

@Repository
public class OfferRepositoryFacade {
	
	@Autowired
	private OfferRepository offerRepository;
	
	@Autowired
	private TradeRepository tradeRepository;
	
	/**
	 * True if all items are associated to the same {@code Trade}.
	 * @param articleIds: {@code Item.articleId} of all {@Items} to verify.
	 */
	public boolean areItemsAssociatedToSameTrade(Integer[] articleIds) {
		Pageable pageable = PersistenceUtil.buildPageable();
		Page<TradeEntity> page = tradeRepository.findInArticleIdsGroupByTrade(articleIds, pageable);
		return page.getNumberOfElements() == 1;
	}
	
	public void delete(Integer offerId) {
		offerRepository.delete(offerId);
	}

	public OfferEntity get(Integer offerId) {
		return offerRepository.findOne(offerId);
	}

	public List<OfferEntity> getByOfferedArticleId(Integer offeredArticleId) {
		return offerRepository.findByOfferedItemArticleId(offeredArticleId);
	}

	public void save(OfferEntity entity) {
		offerRepository.save(entity);
	}
}
