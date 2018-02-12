package com.matchandtrade.rest.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ItemQueryBuilder;
import com.matchandtrade.persistence.criteria.UserQueryBuilder;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeResultEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.trademaximazer.Output;
import com.trademaximazer.TradeMaximizer;

@Component
public class TradeResultService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeResultService.class);
	
	@Autowired
	private SearchService searchService;
	@Autowired
	private TradeRepositoryFacade tradeRepositoryFacade;
	@Autowired
	private OfferService offerService;

	/**
	 * Get the results of a {@code Trade}
	 * @param tradeId
	 * @return
	 */
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
	 * Build a list of entries according to <code>Trade Maximizer</code>'s expected input format.
	 * @param tradeId
	 * @return list of entries for Trade Maximizer
	 */
	private List<String> buildTradeMaximizerInput(Integer tradeId) {
		List<String> tradeMaximizerEntries = new ArrayList<>();
		
		LOGGER.debug("Finding all items for Trade.tradeId: {}", tradeId);
		SearchCriteria itemsCriteria = new SearchCriteria(new Pagination(1, 50));
		itemsCriteria.addCriterion(ItemQueryBuilder.Field.tradeId, tradeId);
		SearchResult<ItemEntity> itemsResult = searchService.search(itemsCriteria, ItemQueryBuilder.class);
		LOGGER.debug("Found items with {} ", itemsResult.getPagination());
		
		itemsResult.getResultList().forEach(item -> {
			//TODO Improve performance, search all users once instead of one by one
			SearchCriteria userCriteria = new SearchCriteria(new Pagination(1,1));
			userCriteria.addCriterion(UserQueryBuilder.Field.itemId, item.getItemId());
			SearchResult<UserEntity> userResult = searchService.search(userCriteria, UserQueryBuilder.class);
			String userName = userResult.getResultList().get(0).getName();
			
			StringBuilder line = new StringBuilder("(" + userName + ") " + item.getItemId() + " :");
			List<OfferEntity> offers = offerService.searchByOfferedItemId(item.getItemId());
			offers.forEach(offer -> {
				line.append(" " + offer.getWantedItem().getItemId());
			});
			tradeMaximizerEntries.add(line.toString());
		});
		
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
		LOGGER.info("Using TradeMaximizer input: {}", tradeMaximizerEntries);
		Output tradeMaximizerOutput = new Output(System.out);
		TradeMaximizer tradeMaximizer = new TradeMaximizer(tradeMaximizerOutput);
		tradeMaximizer.generateResult(tradeMaximizerEntries);
		String result = tradeMaximizerOutput.getOutputString();
		LOGGER.debug("TradeMaximizer output: {}", result);
		return result;
	}
	
}
