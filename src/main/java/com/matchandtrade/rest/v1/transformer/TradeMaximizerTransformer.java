package com.trademaximazer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.matchandtrade.rest.v1.json.TradeResultJson;
import com.matchandtrade.rest.v1.link.ItemLinkAssember;

public class TradeMaximizerTransformer {

	// Utility classes should not have public constructors
	private TradeMaximizerTransformer() { }
	
	/**
	 * Transform a Trade Maximizer result string into a {@code List<TradeResultJson>}.
	 * @param tradeMaximizerResult
	 * @return
	 */
	public static List<TradeResultJson> transform(String tradeMaximizerResult) {
		String tradeMaximizerSummary = tradeMaximizerResult.substring(tradeMaximizerResult.indexOf("ITEM SUMMARY"));
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
		return tradeLines
			.stream()
			.map(s -> transformLine(s))
			.collect(Collectors.toList());
	}
	
	private static TradeResultJson transformLine(String line) {
		TradeResultJson result = new TradeResultJson();
		
		// Build the offering portion
		// offeringTradeMembershipId is the first number in parenthesis
		Integer offeringTradeMembershipId = Integer.parseInt(line.substring(1, line.indexOf(')')));
		// offeringItemId is the number after the first ')'
		Integer offeringItemId = Integer.parseInt(line.substring(line.indexOf(')')+2, ordinalIndexOf(line, " ", 2)));
		result.setOfferingItem(ItemLinkAssember.buildLink(offeringTradeMembershipId, offeringItemId));
		
		// Build the receiving portion
		if (line.contains("receives")) {
			// receivingTradeMembershipId is the second number in parenthesis
			Integer receivingTradeMembershipId = Integer.parseInt(line.substring(line.indexOf("receives (") + 10, ordinalIndexOf(line, ")", 2)));
			// receivingItemId is the number after the second ')'
			int afterSecondCloseParenthesis = ordinalIndexOf(line, ")", 2) + 2;
			Integer receivingItemId = Integer.parseInt(line.substring(afterSecondCloseParenthesis, line.indexOf(' ', afterSecondCloseParenthesis)));
			// Build the result
			result.setReceivingItem(ItemLinkAssember.buildLink(receivingTradeMembershipId, receivingItemId));
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
