package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.service.MembershipService;
import com.matchandtrade.rest.v1.json.TradeResultJson;
import com.matchandtrade.rest.v1.json.TradedArticleJson;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TradeMaximizerTransformer {
	
	@Autowired
	private ArticleService articleService;
	@Autowired
	private MembershipService membershipService;
	@Autowired
	private TradeRepositoryFacade tradeRepositoryFacade;

	/**
	 * Helper class to temporarily hold parsed values from a trade line 
	 */
	private class TradeLinePojo {
		public Integer offeringMembershipId;
		public Integer offeringArticleId;
		public Integer receivingMembershipId;
		public Integer receivingArticleId;
		public Integer sendingMembershipId;
	}

	/**
	 * Transform the output of Trade Maximizer into CSV.
	 * Articles not traded are omitted.
	 *  
	 * @param tradeMaximizerOutput
	 * @return trade result in CSV format
	 * @throws IOException
	 */
	public String toCsv(Integer tradeId, String tradeMaximizerOutput) throws IOException {
		List<String> lines = transformTradeMaximizerToList(tradeMaximizerOutput);

		// Sort trade lines for better CSV presentation 
		sortTradeLinesByOfferingMembershipAndOfferingArticleId(lines);
		
		CSVFormat formatter = CSVFormat.DEFAULT
				.withRecordSeparator("\n")
				.withCommentMarker('#')
				.withHeader("user_id", "user_name", "article_id",  "article_name", "receives",
							"receiving_from_user_id", "receiving_from_user_name", "receiving_article_id", "receiving_article_name", "sends_to",
							"sending_to_user_id", "sending_to_user_name");
		
		StringBuilder csvOutput = new StringBuilder();
		CSVPrinter csvPrinter = formatter.print(csvOutput);
		// Tracks the total of traded articles
		int tradedArticlesCount = 0;
		
		for (String line : lines) {
			TradeLinePojo linePojo = transformLine(line);
			// Articles not traded "receivingMembershipId == null" are omitted
			if (linePojo.receivingMembershipId == null) {
				continue;
			}
			
			List<Object> csvRecord = new ArrayList<>();
			MembershipEntity offeringMembership = membershipService.findByMembershipId(linePojo.offeringMembershipId);
			csvRecord.add(offeringMembership.getUser().getUserId());
			csvRecord.add(offeringMembership.getUser().getName());
			ArticleEntity offeringArticle = articleService.findByArticleId(linePojo.offeringArticleId);
			csvRecord.add(offeringArticle.getArticleId());
			csvRecord.add(offeringArticle.getName());
			if (linePojo.receivingArticleId != null) {
				tradedArticlesCount++;
				csvRecord.add(":RECEIVES:");
				MembershipEntity receivingMembership = membershipService.findByMembershipId(linePojo.receivingMembershipId);
				csvRecord.add(receivingMembership.getUser().getUserId());
				csvRecord.add(receivingMembership.getUser().getName());
				ArticleEntity receivingArticle = articleService.findByArticleId(linePojo.receivingArticleId);
				csvRecord.add(receivingArticle.getArticleId());
				csvRecord.add(receivingArticle.getName());
				csvRecord.add(":SENDS:");
				MembershipEntity sendingMembership = membershipService.findByMembershipId(linePojo.sendingMembershipId);
				csvRecord.add(sendingMembership.getUser().getUserId());
				csvRecord.add(sendingMembership.getUser().getName());
			}
			csvPrinter.printRecord(csvRecord);
		}

		// Handles the Summary portion
		printSummary(tradeId, lines.size(), tradedArticlesCount, csvPrinter);
		csvPrinter.close();
		
		return csvOutput.toString();
	}

	private void printSummary(Integer tradeId, Integer totalOfArticles, int totalOfTradedArticles, CSVPrinter csvPrinter) throws IOException {
		TradeEntity trade = tradeRepositoryFacade.findByTradeId(tradeId);
		csvPrinter.println();
		csvPrinter.printComment("--------------------------------");
		csvPrinter.printComment("Summary of Trade [" + trade.getTradeId() + " : " + trade.getName() + "]");
		csvPrinter.printComment("Total of articles: " + totalOfArticles);
		csvPrinter.printComment("Total of traded articles: " + totalOfTradedArticles);
		csvPrinter.printComment("Total of articles not traded: " + (totalOfArticles - totalOfTradedArticles));
		csvPrinter.printComment("--------------------------------");
		csvPrinter.printComment("");
		csvPrinter.printComment("Below is an example to help you understand the results:");
		csvPrinter.printComment("Sample row:  1,Alice,10,Ticket to Ride,:RECEIVES:,2,Bob,11,Power Grid,:SENDS:,3,Charlie");
		csvPrinter.printComment("Explanation: Alice receives Bob's Power Grid and sends her Ticket to Ride to Charlie");
	}

	/**
	 * Sort trade lines by offeringMembership and articleId 
	 * @param tradeLines
	 */
	private void sortTradeLinesByOfferingMembershipAndOfferingArticleId(List<String> tradeLines) {
		tradeLines.sort((i, j) -> {
			TradeLinePojo iPojo = transformLine(i);
			TradeLinePojo jPojo = transformLine(j);
			if (iPojo.offeringMembershipId > jPojo.offeringMembershipId) {
				return 1;
			} else if (iPojo.offeringMembershipId < jPojo.offeringMembershipId) {
				return -1;
			} else {
				return iPojo.offeringArticleId.compareTo(jPojo.offeringArticleId);
			}
		});
	}

	/**
	 * Transforms the output of Trade Maximizer into a {@code List<String>}
	 * containing only the <i>ARTICLE SUMMARY</i> portion.
	 * @param tradeMaximizerOutput
	 * @return
	 */
	private static List<String> transformTradeMaximizerToList(String tradeMaximizerOutput) {
		String tradeMaximizerSummary = tradeMaximizerOutput.substring(tradeMaximizerOutput.indexOf("ITEM SUMMARY"));
		String[] allLines = tradeMaximizerSummary.split("\n");
		List<String> tradeLines = new ArrayList<>();
		// Get all tradeLines ignoring the HEADER and the ARTICLE SUMARRY portion.
		for (int i = 1; i< allLines.length; i++) {
			// Ignore empty lines or lines which does not start with (
			if (allLines[i].length() <= 1 || !allLines[i].startsWith("(")) {
				continue;
			}
			tradeLines.add(allLines[i]);
		}
		return tradeLines;
	}
	
	/**
	 * Transforms a Trade Maximizer <i>ARTICLE SUMMARY</i> line into a {@code TradeLinePojo};
	 * @param line
	 * @return
	 */
	private TradeLinePojo transformLine(String line) {
		TradeLinePojo result = new TradeLinePojo();
		// Handle the offering portion
		// offeringMembershipId is the first number in parenthesis
		result.offeringMembershipId = Integer.parseInt(line.substring(1, line.indexOf(')')));
		// offeringArticleId is the number after the first ')'
		result.offeringArticleId = Integer.parseInt(line.substring(line.indexOf(')')+2, ordinalIndexOf(line, " ", 2)));
		
		// Handle the receiving portion
		if (line.contains("receives")) {
			// receivingMembershipId is the second number in parenthesis
			result.receivingMembershipId = Integer.parseInt(line.substring(ordinalIndexOf(line, "(", 2) + 1, ordinalIndexOf(line, ")", 2)));
			// receivingArticleId is the number after the second ')'
			int receivingArticleIdIndex = ordinalIndexOf(line, ")", 2) + 2;
			result.receivingArticleId = Integer.parseInt(line.substring(receivingArticleIdIndex, line.indexOf(' ', receivingArticleIdIndex)));
		}
		
		// Handle the sending portion
		if (line.contains("sends")) {
			// sendingMembershipId is the third number in parenthesis
			result.sendingMembershipId = Integer.parseInt(line.substring(ordinalIndexOf(line, "(", 3) + 1, ordinalIndexOf(line, ")", 3)));
		}
		return result;
	}
	
	/*
	 * Finds the n-th index within a String, handling null. This method uses String.indexOf(String).
	 * Implementation of org.apache.commons.lang.StringUtils
	 */
	private static int ordinalIndexOf(String str, String criterion, int ordinalOcurrence) {
	    int pos = str.indexOf(criterion);
	    int n = ordinalOcurrence;
	    while (--n > 0 && pos != -1)
	        pos = str.indexOf(criterion, pos + 1);
	    return pos;
	}
	
	public TradeResultJson toJson(Integer tradeId, String tradeMaximizerOutput) {
		TradeEntity trade = tradeRepositoryFacade.findByTradeId(tradeId);
		List<String> lines = transformTradeMaximizerToList(tradeMaximizerOutput);
		int totalOfTradedArticles = 0;
		
		TradeResultJson result = new TradeResultJson();
		for(String line : lines) {
			TradeLinePojo linePojo = transformLine(line);
			//Skip articles not traded (aka "receivingMembershipId == null")
			if (linePojo.receivingMembershipId == null) {
				continue;
			}
			totalOfTradedArticles++;
			
			MembershipEntity offeringMembership = membershipService.findByMembershipId(linePojo.offeringMembershipId);
			TradedArticleJson tradedArticle = new TradedArticleJson();
			tradedArticle.setUserId(offeringMembership.getUser().getUserId());
			tradedArticle.setUserName(offeringMembership.getUser().getName());
			
			ArticleEntity offeringArticle = articleService.findByArticleId(linePojo.offeringArticleId);
			tradedArticle.setArticleId(offeringArticle.getArticleId());
			tradedArticle.setArticleName(offeringArticle.getName());
			
			MembershipEntity receivingMembership = membershipService.findByMembershipId(linePojo.receivingMembershipId);
			tradedArticle.setReceivingUserId(receivingMembership.getUser().getUserId());
			tradedArticle.setReceivingUserName(receivingMembership.getUser().getName());
			
			ArticleEntity receivingArticle = articleService.findByArticleId(linePojo.receivingArticleId);
			tradedArticle.setReceivingArticleId(receivingArticle.getArticleId());
			tradedArticle.setReceivingArticleName(receivingArticle.getName());
			
			MembershipEntity sendingMemberhip = membershipService.findByMembershipId(linePojo.sendingMembershipId);
			tradedArticle.setSendingUserId(sendingMemberhip.getUser().getUserId());
			tradedArticle.setSendingUserName(sendingMemberhip.getUser().getName());

			result.getTradedArticles().add(tradedArticle);
		}
		
		result.setTradeId(tradeId);
		result.setTradeName(trade.getName());
		result.setTotalOfArticles(lines.size());
		result.setTotalOfTradedArticles(totalOfTradedArticles);
		result.setTotalOfNotTradedArticles(lines.size() - totalOfTradedArticles);
		return result;
	}
	
}
