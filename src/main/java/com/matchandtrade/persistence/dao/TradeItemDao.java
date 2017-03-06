//package com.matchandtrade.persistence.dao;
//
//import org.hibernate.Criteria;
//import org.hibernate.criterion.ProjectionList;
//import org.hibernate.criterion.Projections;
//import org.hibernate.criterion.Restrictions;
//import org.springframework.stereotype.Component;
//
//import com.matchandtrade.common.Criterion;
//import com.matchandtrade.common.SearchCriteria;
//import com.matchandtrade.persistence.entity.TradeItemEntity;
//import com.matchandtrade.persistence.entity.TradeListEntity;
//import com.matchandtrade.persistence.entity.UserEntity;
//
//@Component
//public class TradeItemDao extends Dao<TradeItemEntity> {
//
//	protected Criteria buildSearchCriteria(SearchCriteria searchCriteria) {
//		Criteria result = getCurrentSession().createCriteria(UserEntity.class);
//		String tradeListAlias = UserEntity.Field.tradeLists.toString();
//		String tradeItemAlias = TradeListEntity.Field.tradeItems.toString();
//
//		// Create Alias
//		result.createAlias(UserEntity.Field.tradeLists.toString(), tradeListAlias);
//		result.createAlias(UserEntity.Field.tradeLists.toString() + "." + TradeListEntity.Field.tradeItems.toString(), tradeItemAlias);
//
//		// Projection List
//		tradeListAlias += ".";
//		tradeItemAlias += ".";
//		ProjectionList pl = Projections.projectionList();
//		pl.add(Projections.property(tradeItemAlias + TradeItemEntity.Field.tradeItemId), TradeItemEntity.Field.tradeItemId.toString());
//		pl.add(Projections.property(tradeItemAlias + TradeItemEntity.Field.name), TradeItemEntity.Field.name.toString());
//		pl.add(Projections.property(tradeItemAlias + TradeItemEntity.Field.description), TradeItemEntity.Field.description.toString());
//		result.setProjection(pl);
//
//		// Add Criterion
//		for (Criterion c : searchCriteria.getCriteria()) {
//			if (c.getField().equals(UserEntity.Field.userId)) {
//				result.add(Restrictions.eq(UserEntity.Field.userId.toString(), c.getValue()));
//			}			
//			if (c.getField().equals(TradeItemEntity.Field.tradeItemId)) {
//				result.add(Restrictions.eq(tradeItemAlias + TradeItemEntity.Field.tradeItemId, c.getValue()));
//			}
//		}
//		return result;
//	}
//
//}
