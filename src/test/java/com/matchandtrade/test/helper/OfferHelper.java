package com.matchandtrade.test.helper;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.rest.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Commit
public class OfferHelper {

	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private OfferService offerService;

	public OfferEntity createPersistedEntity(Integer membershipId, Integer offeredArticleId, Integer wantedArticleId) {
		ArticleEntity offeredArticle = articleRepositoryFacade.findByArticleId(offeredArticleId);
		ArticleEntity wantedArticle = articleRepositoryFacade.findByArticleId(wantedArticleId);
		OfferEntity offer = new OfferEntity();
		offer.setOfferedArticle(offeredArticle);
		offer.setWantedArticle(wantedArticle);
		offerService.create(membershipId, offer);
		return offer;
	}

}