package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.common.Sort;
import com.matchandtrade.persistence.criteria.ArticleQueryBuilder;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.TradeMembershipRepositoryFacade;

@Service
public class ItemService {

	@Autowired
	private TradeMembershipRepositoryFacade tradeMembershipRepositoryFacade;
	@Autowired
	private ArticleRepositoryFacade itemRepositoryFacade;
	@Autowired
	private SearchService searchService;
	@Autowired
	private OfferService offerService;

	@Transactional
	public void create(Integer tradeMembershipId, ArticleEntity itemEntity) {
		TradeMembershipEntity tradeMembershipEntity = tradeMembershipRepositoryFacade.get(tradeMembershipId);
		itemRepositoryFacade.save(itemEntity);
		tradeMembershipEntity.getArticles().add(itemEntity);
		tradeMembershipRepositoryFacade.save(tradeMembershipEntity);
	}

	@Transactional
	public void delete(Integer tradeMembershipId, Integer articleId) {
		offerService.deleteOffersForItem(articleId);
		TradeMembershipEntity membership = tradeMembershipRepositoryFacade.get(tradeMembershipId);
		ArticleEntity item = itemRepositoryFacade.get(articleId);
		membership.getArticles().remove(item);
		tradeMembershipRepositoryFacade.save(membership);
		itemRepositoryFacade.delete(articleId);
	}

	public ArticleEntity get(Integer articleId) {
		return itemRepositoryFacade.get(articleId);
	}
	
	public boolean exists(Integer ...articleIds) {
		return itemRepositoryFacade.exists(articleIds);
	}

	@Transactional
	public SearchResult<ArticleEntity> searchByTradeMembershipId(Integer tradeMembershipId, Integer _pageNumber, Integer _pageSize) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		searchCriteria.addCriterion(ArticleQueryBuilder.Field.tradeMembershipId, tradeMembershipId);
		searchCriteria.addSort(ArticleQueryBuilder.Field.name, Sort.Type.ASC);
		return searchService.search(searchCriteria, ArticleQueryBuilder.class);
	}

	public void update(ArticleEntity itemEntity) {
		itemRepositoryFacade.save(itemEntity);
	}

}
