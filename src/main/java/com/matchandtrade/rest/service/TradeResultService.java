package com.matchandtrade.rest.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ItemQueryBuilder;
import com.matchandtrade.persistence.criteria.TradeMembershipQueryBuilder;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.TradeResultEntity;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.transformer.TradeMaximizerTransformer;

import tm.Output;
import tm.TradeMaximizer;

@Component
public class TradeResultService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeResultService.class);
	
	@Autowired
	private SearchService searchService;
	@Autowired
	private TradeRepositoryFacade tradeRepositoryFacade;
	@Autowired
	private OfferService offerService;
	@Autowired
	private TradeMaximizerTransformer tradeMaximizerTransformer;


	private StringBuilder buildOfferLine(TradeMembershipEntity membership, ItemEntity item) {
		StringBuilder line = new StringBuilder("(" + membership.getTradeMembershipId() + ") " + item.getItemId() + " :");
		List<OfferEntity> offers = offerService.searchByOfferedItemId(item.getItemId());
		offers.forEach(offer -> {
			line.append(" " + offer.getWantedItem().getItemId());
		});
		return line;
	}
	
	/**
	 * Build a list of entries according to <code>Trade Maximizer</code>'s expected input format.
	 * @param tradeId
	 * @return list of entries for Trade Maximizer
	 */
	private String buildTradeMaximizerInput(Integer tradeId) {
		StringBuilder tradeMaximizerEntries = new StringBuilder();
		LOGGER.debug("Finding all items for Trade.tradeId: {}", tradeId);
		
		int pageNumber = 1;
		int pageSize = 50;
		SearchResult<ItemEntity> itemsResult = null;
		do {
			itemsResult = searchItems(tradeId, new Pagination(pageNumber++, pageSize));
			itemsResult.getResultList().forEach(item -> {
				TradeMembershipEntity membership = searchMembership(item);
				StringBuilder line = buildOfferLine(membership, item);
				tradeMaximizerEntries.append(line.toString() + "\n");
			});
		} while (itemsResult.getPagination().hasNextPage());
		return tradeMaximizerEntries.toString();
	}

	/**
	 * Build a Trade Maximizer Input string for the {@code tradeId}.
	 * See: trademaximizer/instructions.html
	 * See: https://github.com/chrisokasaki/TradeMaximizer
	 * @param tradeId
	 * @return
	 */
	protected String buildTradeMaximizerOutput(Integer tradeId) {
		// The entries to be passed to Trade Maximizer
		String tradeMaximizerEntries = buildTradeMaximizerInput(tradeId);
		LOGGER.info("Using TradeMaximizer input:\n{}", tradeMaximizerEntries);
		InputStream tradeMaximizerInput = new ByteArrayInputStream(tradeMaximizerEntries.getBytes());
		Output tradeMaximizerOutput = new Output(System.out);
		TradeMaximizer tradeMaximizer = new TradeMaximizer(tradeMaximizerInput, tradeMaximizerOutput);
		tradeMaximizer.run();
		String result = tradeMaximizerOutput.getOutputString();
		LOGGER.debug("TradeMaximizer output:\n{}", result);
		return result;
	}

	/**
	 * Get the results of a {@code Trade}
	 * @param tradeId
	 * @return
	 */
	@Transactional
	public String get(Integer tradeId) {
		TradeEntity trade = tradeRepositoryFacade.get(tradeId);
		trade.setState(TradeEntity.State.GENERATING_RESULTS);
		tradeRepositoryFacade.save(trade);
		if (trade.getResult() == null) {
			String tradeMaximizerOutput = buildTradeMaximizerOutput(tradeId);
			TradeResultEntity tradeResult = new TradeResultEntity();
			tradeResult.setTradeMaximizerOutput(tradeMaximizerOutput);
			try {
				String csv = tradeMaximizerTransformer.toCsv(tradeId, tradeMaximizerOutput);
				LOGGER.debug("Trasnformed TradeMaximizer output into csv:\n{}", csv);
				tradeResult.setCsv(csv);
			} catch (IOException e) {
				throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Not able to generate CSV. Exception message: " + e.getMessage());
			}
			trade.setResult(tradeResult);
			trade.setState(TradeEntity.State.RESULTS_GENERATED);
			tradeRepositoryFacade.save(trade);
		}
		return trade.getResult().getCsv();
	}

	private SearchResult<ItemEntity> searchItems(Integer tradeId, Pagination pagination) {
		SearchCriteria itemsCriteria = new SearchCriteria(pagination);
		itemsCriteria.addCriterion(ItemQueryBuilder.Field.tradeId, tradeId);
		SearchResult<ItemEntity> itemsResult = searchService.search(itemsCriteria, ItemQueryBuilder.class);
		LOGGER.debug("Found items with {} ", pagination);
		return itemsResult;
	}

	private TradeMembershipEntity searchMembership(ItemEntity item) {
		SearchCriteria membershipCriteria = new SearchCriteria(new Pagination(1,1));
		membershipCriteria.addCriterion(TradeMembershipQueryBuilder.Field.itemId, item.getItemId());
		SearchResult<TradeMembershipEntity> membershipResult = searchService.search(membershipCriteria, TradeMembershipQueryBuilder.class);
		TradeMembershipEntity membership = membershipResult.getResultList().get(0);
		return membership;
	}
	
}
