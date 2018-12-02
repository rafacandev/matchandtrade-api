package com.matchandtrade.rest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ArticleQueryBuilder;
import com.matchandtrade.persistence.criteria.MembershipQueryBuilder;
import com.matchandtrade.persistence.entity.*;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.TradeResultJson;
import com.matchandtrade.rest.v1.transformer.TradeMaximizerTransformer;
import com.matchandtrade.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import tm.TradeMaximizer;

import javax.transaction.Transactional;
import java.io.*;
import java.util.List;

@Component
public class TradeResultService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeResultService.class);
	
	@Autowired
	private SearchService<ArticleEntity> searchServiceArticle;
	@Autowired
	private SearchService<MembershipEntity> searchServiceMembership;
	@Autowired
	private TradeRepositoryFacade tradeRepositoryFacade;
	@Autowired
	private OfferService offerService;
	@Autowired
	private TradeMaximizerTransformer tradeMaximizerTransformer;


	private StringBuilder buildOfferLine(MembershipEntity membership, ArticleEntity article) {
		StringBuilder line = new StringBuilder("(" + membership.getMembershipId() + ") " + article.getArticleId() + " :");
		List<OfferEntity> offers = offerService.findByOfferedArticleId(article.getArticleId());
		offers.forEach(offer -> {
			line.append(" " + offer.getWantedArticle().getArticleId());
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
		LOGGER.debug("Finding all articles for Trade.tradeId: {}", tradeId);
		
		int pageNumber = 1;
		int pageSize = 50;
		SearchResult<ArticleEntity> articlesResult = null;
		do {
			articlesResult = findArticlesByTradeId(tradeId, new Pagination(pageNumber++, pageSize));
			articlesResult.getResultList().forEach(article -> {
				MembershipEntity membership = findMembershipByArticle(article);
				StringBuilder line = buildOfferLine(membership, article);
				tradeMaximizerEntries.append(line.toString() + "\n");
			});
		} while (articlesResult.getPagination().hasNextPage());
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
		String tradeMaximizerInputString = buildTradeMaximizerInput(tradeId);
		LOGGER.info("Using TradeMaximizer input:\n{}", tradeMaximizerInputString);
		InputStream tradeMaximizerInput = new ByteArrayInputStream(tradeMaximizerInputString.getBytes());
		OutputStream tradeMaximizerOuput = new ByteArrayOutputStream();
		TradeMaximizer tradeMaximizer = new TradeMaximizer(tradeMaximizerInput, tradeMaximizerOuput);
		tradeMaximizer.run();
		String result = tradeMaximizerOuput.toString();
		try {
			tradeMaximizerOuput.close();
		} catch (IOException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Error when build TradeMaximizer output. " + e.getMessage());
		}
		LOGGER.debug("TradeMaximizer output:\n{}", result);
		return result;
	}

	@Transactional
	public String findByTradeIdAsCsv(Integer tradeId) {
		TradeEntity trade = tradeRepositoryFacade.findByTradeId(tradeId);
		String csv;
		// Return value from database if already exists; otherwhise generate it
		if (trade.getResult().getCsv() != null) {
			return trade.getResult().getCsv();
		}
		try {
			csv = tradeMaximizerTransformer.toCsv(tradeId, trade.getResult().getTradeMaximizerOutput());
		} catch (IOException e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to parse results from tradeId " + tradeId + " in CSV format.");
		}
		LOGGER.debug("Transformed TradeMaximizer output into csv:\n{}", csv);
		trade.getResult().setCsv(csv);
		tradeRepositoryFacade.save(trade);
		return csv;
	}

	
	@Transactional
	public TradeResultJson findByTradeIdAsJson(Integer tradeId) {
		TradeEntity trade = tradeRepositoryFacade.findByTradeId(tradeId);
		
		String tradeResultJsonAsString;
		TradeResultJson tradeResultJson;
		// Return value from database if already exists; otherwhise generate it
		if (trade.getResult() != null && trade.getResult().getJson() != null) {
			tradeResultJsonAsString = trade.getResult().getJson();
			try {
				tradeResultJson = JsonUtil.fromString(tradeResultJsonAsString, TradeResultJson.class);
			} catch (IOException e) {
				throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to parse results from tradeId " + tradeId + " from JSON format.");
			}
		} else {
			tradeResultJson = tradeMaximizerTransformer.toJson(tradeId, trade.getResult().getTradeMaximizerOutput());
			try {
				tradeResultJsonAsString = JsonUtil.toJson(tradeResultJson);
				LOGGER.debug("Transformed TradeMaximizer output into json:\n{}", tradeResultJsonAsString);
			} catch (JsonProcessingException e) {
				throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to parse results from tradeId " + tradeId + " in JSON format.");
			}
		}

		trade.getResult().setJson(tradeResultJsonAsString);
		tradeRepositoryFacade.save(trade);
		return tradeResultJson;
	}

	private SearchResult<ArticleEntity> findArticlesByTradeId(Integer tradeId, Pagination pagination) {
		SearchCriteria articlesCriteria = new SearchCriteria(pagination);
		articlesCriteria.addCriterion(ArticleQueryBuilder.Field.TRADE_ID, tradeId);
		SearchResult<ArticleEntity> result = searchServiceArticle.search(articlesCriteria, ArticleQueryBuilder.class);
		LOGGER.debug("Found articles with {} ", pagination);
		return result;
	}
	
	private MembershipEntity findMembershipByArticle(ArticleEntity article) {
		SearchCriteria membershipCriteria = new SearchCriteria(new Pagination(1,1));
		membershipCriteria.addCriterion(MembershipQueryBuilder.Field.ARTICLE_ID, article.getArticleId());
		SearchResult<MembershipEntity> membershipResult = searchServiceMembership.search(membershipCriteria, MembershipQueryBuilder.class);
		if (membershipResult.getPagination().getTotal() > 1) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "There is more than one Membership for the Article.articleId " + article.getArticleId() + ". I am shocked! This should never ever happen :(");
		} else if (membershipResult.getPagination().getTotal() < 1) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Generating result for an orphan Article.articleId " + article.getArticleId() + ". We are extremelly sad that this happened.");
		}
		return membershipResult.getResultList().get(0);
	}

	@Transactional
	public void generateResults(Integer tradeId) {
		TradeEntity trade = tradeRepositoryFacade.findByTradeId(tradeId);
		trade.setState(TradeEntity.State.GENERATING_RESULTS);
		tradeRepositoryFacade.save(trade);
		String tradeMaximizerOutput = buildTradeMaximizerOutput(tradeId);
		TradeResultEntity tradeResult = new TradeResultEntity();
		tradeResult.setTradeMaximizerOutput(tradeMaximizerOutput);
		trade.setResult(tradeResult);
		tradeRepositoryFacade.save(trade);
		trade.setState(TradeEntity.State.RESULTS_GENERATED);
	}
}
