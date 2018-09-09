package com.matchandtrade.persistence.criteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;

@Component
public class OfferQueryBuilder implements QueryBuilder {

	public enum Field implements com.matchandtrade.persistence.common.Field {
		offeredArticleId("offeredArticle.articleId"),
		membershipId("membership.membershipId"),
		wantedArticleId("wantedArticle.articleId");
		
		private String alias;

		private Field(String alias) {
			this.alias = alias;
		}
		
		@Override
		public String alias() {
			return alias;
		}
	}
	
	@Autowired
	private EntityManager entityManager;
	
    private static final String BASIC_HQL = 
    	  " FROM MembershipEntity AS membership"
    	+ " INNER JOIN membership.offers AS offer"
    	+ " INNER JOIN membership.user AS user"
    	+ " INNER JOIN offer.offeredArticle AS offeredArticle"
    	+ " INNER JOIN offer.wantedArticle AS wantedArticle";

    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*) " + BASIC_HQL);
    	return QueryBuilderUtil.buildQuery(searchCriteria, hql, entityManager, true);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT offer " + BASIC_HQL);
		return QueryBuilderUtil.buildQuery(searchCriteria, hql, entityManager);
	}

}
