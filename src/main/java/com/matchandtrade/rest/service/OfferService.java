package com.matchandtrade.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.OfferQueryBuilder;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.OfferRepositoryFacade;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;

@Service
public class OfferService {
	
	@Autowired
	private OfferRepositoryFacade offerRepositoryFacade;
	@Autowired
	private SearchService searchService;
	@Autowired
	private MembershipRepositoryFacade membershipRepositoryFacade;

	@Transactional
	public boolean areArticlesAssociatedToSameTrade(Integer ...articles) {
		return offerRepositoryFacade.areArticlesAssociatedToSameTrade(articles);
	}

	@Transactional
	public void create(Integer membershipId, OfferEntity offer) {
		MembershipEntity membership = membershipRepositoryFacade.get(membershipId);
		offerRepositoryFacade.save(offer);
		membership.getOffers().add(offer);
		membershipRepositoryFacade.save(membership);
	}

	@Transactional
	public void delete(Integer offerId) {
		MembershipEntity membership = membershipRepositoryFacade.getByOfferId(offerId);
		OfferEntity offer = offerRepositoryFacade.get(offerId);
		membership.getOffers().remove(offer);
		membershipRepositoryFacade.save(membership);
		offerRepositoryFacade.delete(offerId);
	}

	private void delete(OfferQueryBuilder.Field wantedOrOfferedField, Integer articleId) {
		Pagination pagination = new Pagination(1, 50);
		SearchCriteria criteria = new SearchCriteria(pagination);
		criteria.addCriterion(wantedOrOfferedField, articleId);
		do {
			SearchResult<OfferEntity> searchResult = searchService.search(criteria, OfferQueryBuilder.class);
			searchResult.getResultList().forEach(offer -> delete(offer.getOfferId()));
		} while (pagination.hasNextPage());
	}

	/**
	 * Delete all offers where either {@code Offer.wantedArticle.articleId} or {@code Offer.offeredArticle.articleId}
	 * is equals to {@code articleId}
	 * @param membershipId
	 * @param articleId
	 */
	@Transactional
	public void deleteOffersForArticle(Integer articleId) {
		delete(OfferQueryBuilder.Field.WANTED_ARTICLE_ID, articleId);
		delete(OfferQueryBuilder.Field.OFFERED_ARTICLE_ID, articleId);
	}
	
	public OfferEntity get(Integer offerId) {
		return offerRepositoryFacade.get(offerId);
	}

	public SearchResult<OfferEntity> search(Integer membershipId, Integer offeredArticleId, Integer wantedArticleId,
			Integer pageNumber, Integer pageSize) {
		SearchCriteria criteria = new SearchCriteria(new Pagination(pageNumber, pageSize));
		if (membershipId != null) {
			criteria.addCriterion(OfferQueryBuilder.Field.MEMBERSHIP_ID, membershipId);
		}
		if (offeredArticleId != null) {
			criteria.addCriterion(OfferQueryBuilder.Field.OFFERED_ARTICLE_ID, offeredArticleId);
		}
		if (wantedArticleId != null) {
			criteria.addCriterion(OfferQueryBuilder.Field.WANTED_ARTICLE_ID, wantedArticleId);
		}
		return searchService.search(criteria, OfferQueryBuilder.class);
	}

	public List<OfferEntity> searchByOfferedArticleId(Integer offeredArticleId) {
		return offerRepositoryFacade.getByOfferedArticleId(offeredArticleId);
	}

}
