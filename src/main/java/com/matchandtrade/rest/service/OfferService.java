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
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.facade.OfferRepositoryFacade;
import com.matchandtrade.persistence.facade.TradeMembershipRepositoryFacade;

@Service
public class OfferService {
	
	@Autowired
	private OfferRepositoryFacade offerRepositoryFacade;
	@Autowired
	private SearchService searchService;
	@Autowired
	private TradeMembershipRepositoryFacade tradeMembershipRepositoryFacade;

	@Transactional
	public boolean areItemsAssociatedToSameTrade(Integer ...items) {
		return offerRepositoryFacade.areItemsAssociatedToSameTrade(items);
	}

	@Transactional
	public void create(Integer tradeMembershipId, OfferEntity offer) {
		TradeMembershipEntity tradeMembership = tradeMembershipRepositoryFacade.get(tradeMembershipId);
		offerRepositoryFacade.save(offer);
		tradeMembership.getOffers().add(offer);
		tradeMembershipRepositoryFacade.save(tradeMembership);
	}

	@Transactional
	public void delete(Integer offerId) {
		TradeMembershipEntity membership = tradeMembershipRepositoryFacade.getByOfferId(offerId);
		OfferEntity offer = offerRepositoryFacade.get(offerId);
		membership.getOffers().remove(offer);
		tradeMembershipRepositoryFacade.save(membership);
		offerRepositoryFacade.delete(offerId);
	}

	private void delete(OfferQueryBuilder.Field wantedOrOfferedField, Integer itemId) {
		Pagination pagination = new Pagination(1, 50);
		SearchCriteria criteria = new SearchCriteria(pagination);
		criteria.addCriterion(wantedOrOfferedField, itemId);
		do {
			SearchResult<OfferEntity> searchResult = searchService.search(criteria, OfferQueryBuilder.class);
			searchResult.getResultList().forEach(offer -> delete(offer.getOfferId()));
		} while (pagination.hasNextPage());
	}

	/**
	 * Delete all offers where either {@code Offer.wantedItem.itemId} or {@code Offer.offeredItem.itemId}
	 * is equals to {@code itemId}
	 * @param tradeMembershipId
	 * @param itemId
	 */
	@Transactional
	public void deleteOffersForItem(Integer itemId) {
		delete(OfferQueryBuilder.Field.wantedItemId, itemId);
		delete(OfferQueryBuilder.Field.offeredItemId, itemId);
	}
	
	public OfferEntity get(Integer offerId) {
		return offerRepositoryFacade.get(offerId);
	}

	public SearchResult<OfferEntity> search(Integer tradeMembershipId, Integer offeredItemId, Integer wantedItemId,
			Integer pageNumber, Integer pageSize) {
		SearchCriteria criteria = new SearchCriteria(new Pagination(pageNumber, pageSize));
		if (tradeMembershipId != null) {
			criteria.addCriterion(OfferQueryBuilder.Field.tradeMembershipId, tradeMembershipId);
		}
		if (offeredItemId != null) {
			criteria.addCriterion(OfferQueryBuilder.Field.offeredItemId, offeredItemId);
		}
		if (wantedItemId != null) {
			criteria.addCriterion(OfferQueryBuilder.Field.wantedItemId, wantedItemId);
		}
		return searchService.search(criteria, OfferQueryBuilder.class);
	}

	public List<OfferEntity> searchByOfferedItemId(Integer offeredItemId) {
		return offerRepositoryFacade.getByOfferedItemId(offeredItemId);
	}

}
