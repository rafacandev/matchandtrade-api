package com.matchandtrade.persistence.criteria;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.dto.ArticleAndMembershipIdDto;
import com.matchandtrade.persistence.entity.ArticleEntity;

@Component
public class ArticleRecipeQueryBuilder implements QueryBuilder {

	public enum Field implements com.matchandtrade.persistence.common.Field {
		TRADE_ID("membership.trade.tradeId"), TRADE_MEMBERSHIP_ID("membership.membershipId");

		private String alias;
		
		Field(String alias) {
			this.alias = alias;
		}
		
		@Override
		public String alias() {
			return alias;
		}
	}
	
    private static final String BASIC_HQL = "FROM MembershipEntity AS membership"
    		+ " INNER JOIN membership.articles AS article";

    @Autowired
    private EntityManager entityManager;

    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*) " + BASIC_HQL);
    	return QueryBuilderUtil.buildQuery(searchCriteria, hql, entityManager, true);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
		StringBuilder hql = new StringBuilder("SELECT membership.membershipId, article " + BASIC_HQL);
		return QueryBuilderUtil.buildQuery(searchCriteria, hql, entityManager);
	}

	public ResultTransformer makeResultTransformer() {
		return new ArticleAndMembershipId();
	}
	
	// TODO: Review this, is there a simpler way to solve this problem? 
	public class ArticleAndMembershipId implements ResultTransformer {
		private static final long serialVersionUID = -912373493890582112L;

		@Override
		public Object transformTuple(Object[] tuple, String[] arg1) {
			Integer membershipId = (Integer) tuple[0];
			ArticleEntity article = (ArticleEntity) tuple[1];
			return new ArticleAndMembershipIdDto(article, membershipId);
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public List transformList(List collection) {
			return collection;
		}		
	}
	
}
