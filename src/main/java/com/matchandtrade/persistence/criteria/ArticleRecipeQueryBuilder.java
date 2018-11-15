package com.matchandtrade.persistence.criteria;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.dto.ArticleAndMembershipIdDto;
import com.matchandtrade.persistence.entity.ArticleEntity;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.List;

@Component
public class ArticleRecipeQueryBuilder implements QueryBuilder {

	@Autowired
	private QueryBuilderHelper queryBuilderHelper;

	public enum Field implements com.matchandtrade.persistence.common.Field {
		TRADE_ID("membership.trade.tradeId"), TRADE_MEMBERSHIP_ID("membership.membershipId");

		private String alias;
		
		Field(String alias) { this.alias = alias; }
		
		@Override
		public String alias() { return alias; }
	}
	
    private static final String BASIC_HQL = "FROM MembershipEntity AS membership"
    		+ " INNER JOIN membership.articles AS article";

    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*) " + BASIC_HQL);
    	return queryBuilderHelper.buildQuery(searchCriteria, hql, true);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
		StringBuilder hql = new StringBuilder("SELECT membership.membershipId, article " + BASIC_HQL);
		return queryBuilderHelper.buildQuery(searchCriteria, hql);
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
