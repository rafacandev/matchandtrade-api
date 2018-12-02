package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ListingService {
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private MembershipService membershipService;
	@Autowired
	private SearchService<MembershipEntity> searchService;

	@Transactional
	public void create(Integer membershipId, Integer articleId) {
		MembershipEntity membership = membershipService.findByMembershipId(membershipId);
		ArticleEntity article = articleRepositoryFacade.findByArticleId(articleId);
		membership.getArticles().add(article);
		membershipService.save(membership);
	}

	@Transactional
	public void delete(Integer membershipId, Integer articleId) {
		MembershipEntity membership = membershipService.findByMembershipId(membershipId);
		ArticleEntity article = articleRepositoryFacade.findByArticleId(articleId);
		membership.getArticles().remove(article);
	}
}
