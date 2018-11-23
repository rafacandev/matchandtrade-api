package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.MembershipQueryBuilder;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ListingService {

	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private MembershipRepositoryFacade membershipRepositoryFacade;
	@Autowired
	private SearchService<MembershipEntity> searchService;

	@Transactional
	public void create(Integer membershipId, Integer articleId) {
		MembershipEntity membership = membershipRepositoryFacade.find(membershipId);
		ArticleEntity article = articleRepositoryFacade.find(articleId);
		membership.getArticles().add(article);
		membershipRepositoryFacade.save(membership);
	}

	@Transactional
	public void delete(Integer membershipId, Integer articleId) {
		MembershipEntity membership = membershipRepositoryFacade.find(membershipId);
		ArticleEntity article = articleRepositoryFacade.find(articleId);
		membership.getArticles().remove(article);
	}

	public SearchResult<MembershipEntity> findMembershipByUserIdAndMembershpiId(Integer userId, Integer membershipId) {
		SearchCriteria criteria = new SearchCriteria(new Pagination());
		criteria.addCriterion(MembershipQueryBuilder.Field.USER_ID, userId);
		criteria.addCriterion(MembershipQueryBuilder.Field.MEMBERSHIP_ID, membershipId);
		return searchService.search(criteria, MembershipQueryBuilder.class);
	}

}
