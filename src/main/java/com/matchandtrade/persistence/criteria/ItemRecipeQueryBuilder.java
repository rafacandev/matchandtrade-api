package com.matchandtrade.persistence.criteria;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.dto.ItemAndTradeMembershipIdDto;
import com.matchandtrade.persistence.entity.ItemEntity;

@Component
public class ItemRecipeQueryBuilder implements QueryBuilder {

	public enum Field implements com.matchandtrade.persistence.common.Field {
		TRADE_ID("tradeMembership.trade.tradeId"), TRADE_MEMBERSHIP_ID("tradeMembership.tradeMembershipId");

		private String alias;
		
		Field(String alias) {
			this.alias = alias;
		}
		
		@Override
		public String alias() {
			return alias;
		}
	}
	
    private static final String BASIC_HQL = "FROM TradeMembershipEntity AS tradeMembership"
    		+ " INNER JOIN tradeMembership.items AS item";

    @Autowired
    private EntityManager entityManager;

    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*) " + BASIC_HQL);
    	return QueryBuilderUtil.parameterizeQuery(searchCriteria.getCriteria(), hql, entityManager);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
		StringBuilder hql = new StringBuilder("SELECT tradeMembership.tradeMembershipId, item " + BASIC_HQL);
		return QueryBuilderUtil.parameterizeQuery(searchCriteria.getCriteria(), hql, entityManager);
	}

	public ResultTransformer makeResultTransformer() {
		return new ItemAndTradeMembershipId();
	}
	
	public class ItemAndTradeMembershipId implements ResultTransformer {
		private static final long serialVersionUID = -912373493890582112L;

		@Override
		public Object transformTuple(Object[] tuple, String[] arg1) {
			Integer tradeMembershipId = (Integer) tuple[0];
			ItemEntity item = (ItemEntity) tuple[1];
			return new ItemAndTradeMembershipIdDto(item, tradeMembershipId);
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public List transformList(List collection) {
			return collection;
		}		
	}
	
}
