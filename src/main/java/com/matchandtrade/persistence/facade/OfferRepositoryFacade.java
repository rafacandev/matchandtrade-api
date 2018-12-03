package com.matchandtrade.persistence.facade;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.PersistenceUtil;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.repository.OfferRepository;
import com.matchandtrade.persistence.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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

	public SearchResult<OfferEntity> findByOfferedArticleId(Integer offeredArticleId, Pagination pagination) {
		Pageable pageable = PersistenceUtil.buildPageable(pagination.getNumber(), pagination.getSize());
		Page<OfferEntity> page = offerRepository.findByOfferedArticleArticleId(offeredArticleId, pageable);
		return PersistenceUtil.buildSearchResult(pageable, page);
	}

	public void save(OfferEntity entity) {
		offerRepository.save(entity);
	}
}
