package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.MembershipQueryBuilder;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.SearchService;
import com.matchandtrade.rest.v1.json.ListingJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListingValidator {

	@Autowired
	ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	SearchService searchService;

	public void validateDelete(Integer userId, ListingJson listing) {
		verifyThatMembershipBelongsToUser(userId, listing.getMebershipId());
		verifyThatArticleBelongsToUser(userId, listing.getArticleId());
	}

	public void validatePost(Integer userId, ListingJson listing) {
		verifyThatMembershipBelongsToUser(userId, listing.getMebershipId());
		verifyThatArticleBelongsToUser(userId, listing.getArticleId());
	}

	private void verifyThatArticleBelongsToUser(Integer userId, Integer articleId) {
		ArticleEntity article = articleRepositoryFacade.getByUserIdAndArticleId(userId, articleId);
		if (article == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, String.format("Article.articleId: %d does not belong to User.userId: %d", articleId, userId));
		}
	}

	private void verifyThatMembershipBelongsToUser(Integer userId, Integer membershipId) {
		SearchCriteria criteria = new SearchCriteria(new Pagination());
		criteria.addCriterion(MembershipQueryBuilder.Field.userId, userId);
		criteria.addCriterion(MembershipQueryBuilder.Field.membershipId, membershipId);
		SearchResult<MembershipEntity> searchResult = searchService.search(criteria, MembershipQueryBuilder.class);
		if (searchResult.getPagination().getTotal() < 1) {
			throw new RestException(HttpStatus.BAD_REQUEST, String.format("Membership.membershipId: %d does not belong to User.userId: %d", membershipId, userId));
		}
	}

}
