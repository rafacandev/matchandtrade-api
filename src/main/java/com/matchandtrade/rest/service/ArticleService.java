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
public class ArticleService {

	@Autowired
	private TradeMembershipRepositoryFacade tradeMembershipRepositoryFacade;
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private SearchService searchService;
	@Autowired
	private OfferService offerService;

	@Transactional
	public void create(Integer tradeMembershipId, ArticleEntity articleEntity) {
		TradeMembershipEntity tradeMembershipEntity = tradeMembershipRepositoryFacade.get(tradeMembershipId);
		articleRepositoryFacade.save(articleEntity);
		tradeMembershipEntity.getArticles().add(articleEntity);
		tradeMembershipRepositoryFacade.save(tradeMembershipEntity);
	}

	@Transactional
	public void delete(Integer tradeMembershipId, Integer articleId) {
		offerService.deleteOffersForArticle(articleId);
		TradeMembershipEntity membership = tradeMembershipRepositoryFacade.get(tradeMembershipId);
		ArticleEntity article = articleRepositoryFacade.get(articleId);
		membership.getArticles().remove(article);
		tradeMembershipRepositoryFacade.save(membership);
		articleRepositoryFacade.delete(articleId);
	}

	public ArticleEntity get(Integer articleId) {
		return articleRepositoryFacade.get(articleId);
	}
	
	public boolean exists(Integer ...articleIds) {
		return articleRepositoryFacade.exists(articleIds);
	}

	@Transactional
	public SearchResult<ArticleEntity> searchByTradeMembershipId(Integer tradeMembershipId, Integer _pageNumber, Integer _pageSize) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		searchCriteria.addCriterion(ArticleQueryBuilder.Field.tradeMembershipId, tradeMembershipId);
		searchCriteria.addSort(ArticleQueryBuilder.Field.name, Sort.Type.ASC);
		return searchService.search(searchCriteria, ArticleQueryBuilder.class);
	}

	public void update(ArticleEntity article) {
		articleRepositoryFacade.save(article);
	}

}
