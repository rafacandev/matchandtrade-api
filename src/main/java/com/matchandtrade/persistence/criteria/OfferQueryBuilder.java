package com.matchandtrade.persistence.criteria;

import com.matchandtrade.persistence.common.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Query;

@Component
public class OfferQueryBuilder implements QueryBuilder {

	@Autowired
	private QueryBuilderHelper queryBuilderHelper;

	public enum Field implements com.matchandtrade.persistence.common.Field {
		OFFERED_ARTICLE_ID("offeredArticle.articleId"),
		MEMBERSHIP_ID("membership.membershipId"),
		WANTED_ARTICLE_ID("wantedArticle.articleId");
		
		private String alias;

		Field(String alias) { this.alias = alias; }
		
		@Override
		public String alias() { return alias; }
	}
	
    private static final String BASIC_HQL =
    	  " FROM MembershipEntity AS membership"
    	+ " INNER JOIN membership.offers AS offer"
    	+ " INNER JOIN membership.user AS user"
    	+ " INNER JOIN offer.offeredArticle AS offeredArticle"
    	+ " INNER JOIN offer.wantedArticle AS wantedArticle";

    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*) " + BASIC_HQL);
    	return queryBuilderHelper.buildQuery(searchCriteria, hql, true);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT offer " + BASIC_HQL);
		return queryBuilderHelper.buildQuery(searchCriteria, hql);
	}

}
