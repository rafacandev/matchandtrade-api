package com.matchandtrade.rest.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.TradeMembershipQueryBuilder;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.WantItemEntity;
import com.trademaximazer.Output;
import com.trademaximazer.TradeMaximizer;

@Component
public class TradeResultService {

	@Autowired
	private SearchService searchService;

	@Transactional
	public String get(Integer tradeId) {
		List<String> wItems = new ArrayList<>();
		SearchResult<TradeMembershipEntity> tradeMemberships = searchByTradeIdUserId(tradeId, null, 1, 50);
		for (TradeMembershipEntity tme : tradeMemberships.getResultList()) {
			for (ItemEntity ie : tme.getItems()) {
				StringBuilder wantEntry = new StringBuilder("(" + tme.getTradeMembershipId() + ")");
				wantEntry.append(" " + ie.getItemId() + " :");
				for (WantItemEntity wie : ie.getWantItems()) {
					wantEntry.append(" " + wie.getItem().getItemId());
				}
				wItems.add(wantEntry.toString());
			}
		}
		
		Output tradeMaximizerOutput = new Output(System.out);
		TradeMaximizer tradeMaximizer = new TradeMaximizer(tradeMaximizerOutput);
		tradeMaximizer.generateResult(wItems);
		return tradeMaximizerOutput.getOutputString();
	}
	
	
	private SearchResult<TradeMembershipEntity> searchByTradeIdUserId(Integer tradeId, Integer userId, Integer _pageNumber, Integer _pageSize) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		if (userId != null) {
			searchCriteria.addCriterion(TradeMembershipQueryBuilder.Field.userId, userId);
		}
		if (tradeId != null) {
			searchCriteria.addCriterion(TradeMembershipQueryBuilder.Field.tradeId, tradeId);
		}
		// Delegate to Repository layer
		return searchService.search(searchCriteria, TradeMembershipQueryBuilder.class);
	}

}
