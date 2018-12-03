package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.OfferQueryBuilder;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.facade.OfferRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OfferService {
	@Autowired
	private OfferRepositoryFacade offerRepositoryFacade;
	@Autowired
	private SearchService<OfferEntity> searchService;
	@Autowired
	private MembershipService membershipService;

	@Transactional
	public void create(Integer membershipId, OfferEntity offer) {
		MembershipEntity membership = membershipService.findByMembershipId(membershipId);
		offerRepositoryFacade.save(offer);
		membership.getOffers().add(offer);
		membershipService.save(membership);
	}

	@Transactional
	public void delete(Integer offerId) {
		MembershipEntity membership = membershipService.findByOfferId(offerId);
		OfferEntity offer = offerRepositoryFacade.findByOfferId(offerId);
		membership.getOffers().remove(offer);
		membershipService.save(membership);
		offerRepositoryFacade.delete(offerId);
	}

	public OfferEntity findByOfferId(Integer offerId) {
		return offerRepositoryFacade.findByOfferId(offerId);
	}

	public SearchResult<OfferEntity> findByOfferedArticleId(Integer offeredArticleId, Pagination pagination) {
		return offerRepositoryFacade.findByOfferedArticleId(offeredArticleId, pagination);
	}

	public SearchResult<OfferEntity> findByMembershipIdOfferedArticleIdWantedArticleId(
			Integer membershipId,
			Integer offeredArticleId,
			Integer wantedArticleId,
			Integer pageNumber,
			Integer pageSize) {
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
}
