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
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;

@Service
public class MembershipArticleService {

	@Autowired
	private MembershipRepositoryFacade membershipRepositoryFacade;
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private SearchService searchService;
	@Autowired
	private OfferService offerService;

	@Transactional
	public void create(Integer membershipId, ArticleEntity articleEntity) {
		MembershipEntity membershipEntity = membershipRepositoryFacade.get(membershipId);
		articleRepositoryFacade.save(articleEntity);
		membershipEntity.getArticles().add(articleEntity);
		membershipRepositoryFacade.save(membershipEntity);
	}

	@Transactional
	public void delete(Integer membershipId, Integer articleId) {
		offerService.deleteOffersForArticle(articleId);
		MembershipEntity membership = membershipRepositoryFacade.get(membershipId);
		ArticleEntity article = articleRepositoryFacade.get(articleId);
		membership.getArticles().remove(article);
		membershipRepositoryFacade.save(membership);
		articleRepositoryFacade.delete(articleId);
	}

	public ArticleEntity get(Integer articleId) {
		return articleRepositoryFacade.get(articleId);
	}

	public boolean exists(Integer ...articleIds) {
		return articleRepositoryFacade.exists(articleIds);
	}

	@Transactional
	public SearchResult<ArticleEntity> searchByMembershipId(Integer membershipId, Integer _pageNumber, Integer _pageSize) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		searchCriteria.addCriterion(ArticleQueryBuilder.Field.membershipId, membershipId);
		searchCriteria.addSort(ArticleQueryBuilder.Field.name, Sort.Type.ASC);
		return searchService.search(searchCriteria, ArticleQueryBuilder.class);
	}

	public void update(ArticleEntity article) {
		articleRepositoryFacade.save(article);
	}

}
