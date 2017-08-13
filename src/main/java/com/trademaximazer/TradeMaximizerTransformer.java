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
		Integer offeringTradeMembershipId = Integer.parseInt(line.substring(1, line.indexOf(")")));
		// Remove the content before ')'
		line = line.substring(line.indexOf(")") + 2);
		// offeringItemId is the number after the first ')'
		Integer offeringItemId = Integer.parseInt(line.substring(0, line.indexOf(" ")));
		result.setOfferingItem(ItemLinkAssember.buildLink(offeringTradeMembershipId, offeringItemId));
		
		// Build the receiving portion
		if (line.contains("receives")) {
			// Remove the content before 'receives ('
			line = line.substring(line.indexOf("receives (") + 10);
			// receivingTradeMembershipId is the second number in parenthesis
			Integer receivingTradeMembershipId = Integer.parseInt(line.substring(0, line.indexOf(")")));
			// receivingTradeMembershipId is the second number in parenthesis
			line = line.substring(line.indexOf(")") + 2);
			String receivingTradeMembershipIdString = line.substring(0, line.indexOf(" "));
			Integer receivingItemId = Integer.parseInt(receivingTradeMembershipIdString);
			// Build the result
			result.setReceivingItem(ItemLinkAssember.buildLink(receivingTradeMembershipId, receivingItemId));
		}
		return result;
	}
	
}
