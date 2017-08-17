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
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.TradeResultEntity;
import com.matchandtrade.persistence.entity.WantItemEntity;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.trademaximazer.Output;
import com.trademaximazer.TradeMaximizer;

@Component
public class TradeResultService {

	@Autowired
	private SearchService searchService;
	@Autowired
	private TradeRepositoryFacade tradeRepositoryFacade;

	@Transactional
	public String get(Integer tradeId) {
		TradeEntity trade = tradeRepositoryFacade.get(tradeId);
		if (trade.getResult() == null) {
			String result = buildTradeMaximizerOutput(tradeId);
			TradeResultEntity tradeResult = new TradeResultEntity();
			tradeResult.setText(result);
			trade.setResult(tradeResult);
			tradeRepositoryFacade.save(trade);
		}
		return trade.getResult().getText();
	}
	
	/**
	 * Build a list of entries for Trade Maximizer for the {@code tradeId}.
	 * It fetches all tradeMemberships, items and wanted items for the given {@code tradeId}
	 * and transforms it in the expected Trade Maximizer's input.
	 * @param tradeId
	 * @return list of entries for Trade Maximizer
	 */
	private List<String> buildTradeMaximizerInput(Integer tradeId) {
		List<String> tradeMaximizerEntries = new ArrayList<>();
		boolean hasNextPage = false;
		Integer currentPageNumber = 1;
		do {
			SearchResult<TradeMembershipEntity> tradeMemberships = searchTradeMembershipByTradeId(tradeId, currentPageNumber, 50);
			for (TradeMembershipEntity tradeMembership : tradeMemberships.getResultList()) {
				for (ItemEntity item : tradeMembership.getItems()) {
					StringBuilder tradeMaximizerEntry = new StringBuilder("(" + tradeMembership.getTradeMembershipId() + ")");
					tradeMaximizerEntry.append(" " + item.getItemId() + " :");
					for (WantItemEntity wantItem : item.getWantItems()) {
						tradeMaximizerEntry.append(" " + wantItem.getItem().getItemId());
					}
					tradeMaximizerEntries.add(tradeMaximizerEntry.toString());
				}
			}
			hasNextPage = tradeMemberships.getPagination().hasNextPage();
			currentPageNumber++;
		} while (hasNextPage);
		return tradeMaximizerEntries;
	}

	/**
	 * Build a Trade Maximizer Input string for the {@code tradeId}.
	 * See: trademaximizer/instructions.html
	 * See: https://github.com/chrisokasaki/TradeMaximizer
	 * @param tradeId
	 * @return
	 */
	private String buildTradeMaximizerOutput(Integer tradeId) {
		// The entries to be passed to Trade Maximizer
		List<String> tradeMaximizerEntries = buildTradeMaximizerInput(tradeId);
		
		Output tradeMaximizerOutput = new Output(System.out);
		TradeMaximizer tradeMaximizer = new TradeMaximizer(tradeMaximizerOutput);
		tradeMaximizer.generateResult(tradeMaximizerEntries);
		return tradeMaximizerOutput.getOutputString();
	}
	
	private SearchResult<TradeMembershipEntity> searchTradeMembershipByTradeId(Integer tradeId, Integer _pageNumber, Integer _pageSize) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		if (tradeId != null) {
			searchCriteria.addCriterion(TradeMembershipQueryBuilder.Field.tradeId, tradeId);
		}
		// Delegate to Repository layer
		return searchService.search(searchCriteria, TradeMembershipQueryBuilder.class);
	}

}
