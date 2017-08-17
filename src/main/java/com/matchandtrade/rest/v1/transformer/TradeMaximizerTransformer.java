package com.matchandtrade.rest.v1.transformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.facade.ItemRepositoryFacade;
import com.matchandtrade.persistence.facade.TradeMembershipRepositoryFacade;

@Component
public class TradeMaximizerTransformer {
	
	@Autowired
	private ItemRepositoryFacade itemRepositoryFacade;
	@Autowired
	private TradeMembershipRepositoryFacade tradeMembershipRepositoryFacade;

	/**
	 * Helper class to temporarily hold parsed values from a trade line 
	 */
	private class TradeLinePojo {
		public Integer offeringTradeMembershipId;
		public Integer offeringItemId;
		public Integer receivingTradeMembershipId;
		public Integer receivingItemId;
	}

	/**
	 * Transform the output of Trade Maximizer into CSV.
	 * Items not traded are omitted.
	 *  
	 * @param tradeMaximizerOutput
	 * @return trade result in CSV format
	 * @throws IOException
	 */
	public String transform(String tradeMaximizerOutput) throws IOException {
		List<String> tradeLines = transformTradeMaximizerToList(tradeMaximizerOutput);
		CSVFormat formatter = CSVFormat.DEFAULT
				.withRecordSeparator("\n")
				.withCommentMarker('#')
				.withHeader("offering_user_id",  "offering_user_name",
							"offering_item_id",  "offering_item_name",
							"receiving_user_id", "receiving_user_name",
							"receiving_item_id", "receiving_item_name");
		
		StringBuilder csvOutput = new StringBuilder();
		CSVPrinter csvPrinter = formatter.print(csvOutput);
		// Tracks the total of trade items
		int tradedItemsCount = 0;
		for (String tradeLine : tradeLines) {
			TradeLinePojo tradeLinePojo = transformLine(tradeLine);
			//Items not traded "receivingTradeMembershipId == null" are omitted
			if (tradeLinePojo.receivingTradeMembershipId == null) {
				continue;
			}
			List<Object> csvRecord = new ArrayList<>();
			TradeMembershipEntity offeringTradeMembership = tradeMembershipRepositoryFacade.get(tradeLinePojo.offeringTradeMembershipId);
			csvRecord.add(offeringTradeMembership.getUser().getUserId());
			csvRecord.add(offeringTradeMembership.getUser().getName());
			ItemEntity offeringItem = itemRepositoryFacade.get(tradeLinePojo.offeringItemId);
			csvRecord.add(offeringItem.getItemId());
			csvRecord.add(offeringItem.getName());
			if (tradeLinePojo.receivingItemId != null) {
				tradedItemsCount++;
				TradeMembershipEntity receivingTradeMembership = tradeMembershipRepositoryFacade.get(tradeLinePojo.receivingTradeMembershipId);
				csvRecord.add(receivingTradeMembership.getUser().getUserId());
				csvRecord.add(receivingTradeMembership.getUser().getName());
				ItemEntity receivingItem = itemRepositoryFacade.get(tradeLinePojo.receivingItemId);
				csvRecord.add(receivingItem.getItemId());
				csvRecord.add(receivingItem.getName());
			}
			csvPrinter.printRecord(csvRecord);
		}

		// Handles the Summary portion
		TradeMembershipEntity offeringTradeMembership = tradeMembershipRepositoryFacade.get(transformLine(tradeLines.get(0)).offeringTradeMembershipId);
		TradeEntity trade = offeringTradeMembership.getTrade();
		csvPrinter.println();
		csvPrinter.printComment("#############################");
		csvPrinter.printComment("Summary of Trade [" + trade.getTradeId() + " : " + trade.getName() + "]");
		csvPrinter.printComment("Total of items: " + tradeLines.size());
		csvPrinter.printComment("Total of traded items: " + tradedItemsCount);
		csvPrinter.printComment("Total of items not traded: " + (tradeLines.size() - tradedItemsCount));
		csvPrinter.printComment("#############################");
		csvPrinter.close();
		
		return csvOutput.toString();
	}

	/**
	 * Transforms the output of Trade Maximizer into a {@code List<String>}
	 * containing only the <i>ITEM SUMMARY</i> portion.
	 * @param tradeMaximizerOutput
	 * @return
	 */
	private static List<String> transformTradeMaximizerToList(String tradeMaximizerOutput) {
		String tradeMaximizerSummary = tradeMaximizerOutput.substring(tradeMaximizerOutput.indexOf("ITEM SUMMARY"));
		String[] allLines = tradeMaximizerSummary.split("\n");
		List<String> tradeLines = new ArrayList<>();
		// Get all tradeLines ignoring the HEADER and the ITEM SUMARRY portion.
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
	 * Transforms a Trade Maximizer <i>ITEM SUMMARY</i> line into a {@code TradeLinePojo};
	 * @param line
	 * @return
	 */
	private TradeLinePojo transformLine(String line) {
		TradeLinePojo result = new TradeLinePojo();
		// Build the offering portion
		// offeringTradeMembershipId is the first number in parenthesis
		result.offeringTradeMembershipId = Integer.parseInt(line.substring(1, line.indexOf(')')));
		// offeringItemId is the number after the first ')'
		result.offeringItemId = Integer.parseInt(line.substring(line.indexOf(')')+2, ordinalIndexOf(line, " ", 2)));
		// Build the receiving portion
		if (line.contains("receives")) {
			// receivingTradeMembershipId is the second number in parenthesis
			result.receivingTradeMembershipId = Integer.parseInt(line.substring(ordinalIndexOf(line, "(", 2) + 1, ordinalIndexOf(line, ")", 2)));
			// receivingItemId is the number after the second ')'
			int receivingItemIdIndex = ordinalIndexOf(line, ")", 2) + 2;
			result.receivingItemId = Integer.parseInt(line.substring(receivingItemIdIndex, line.indexOf(' ', receivingItemIdIndex)));
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
	
}
