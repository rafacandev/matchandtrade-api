package com.matchandtrade.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.facade.OfferRepositoryFacade;

@Service
public class OfferService {
	
	@Autowired
	private OfferRepositoryFacade offerRepositoryFacade;

	@Transactional
	public boolean areItemsAssociatedToSameTrade(Integer ...items) {
		return offerRepositoryFacade.areItemsAssociatedToSameTrade(items);
	}

	@Transactional
	public void create(OfferEntity offer) {
		offerRepositoryFacade.save(offer);
	}

	public OfferEntity get(Integer offerId) {
		return offerRepositoryFacade.get(offerId);
	}
	
	public List<OfferEntity> searchByOfferedItemId(Integer offeredItemId) {
		return offerRepositoryFacade.getByOfferedItemId(offeredItemId);
	}

}
