package com.matchandtrade.trademaximizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.matchandtrade.rest.v1.json.TradeResultJson;
import com.matchandtrade.rest.v1.link.ItemLinkAssember;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.trademaximazer.TradeMaximizerTransformer;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeMaximizerTransformerUT {
	
	private static final String TRADE_MAXIMIZER_RESULT = 
			"TradeMaximizer Version 1.3a\n" +
			"\n" +
			"TRADE LOOPS (4 total trades):\n" +
			"\n" +
			"(1) 1 receives (2) 3\n" +
			"(2) 3 receives (1) 1\n" +
			"\n" +
			"(2) 4 receives (3) 6\n" +
			"(3) 6 receives (2) 4\n" +
			"\n" +
			"ITEM SUMMARY (4 total trades):\n" +
			"\n" +
			"(1) 1 receives (2) 3  and sends to (2) 3\n" +
			"(1) 2             does not trade\n" +
			"(2) 3 receives (1) 1 and sends to (1) 1\n" +
			"(2) 4 receives (3) 6 and sends to (3) 6\n" +
			"(2) 5             does not trade\n" +
			"(3) 6 receives (2) 4 and sends to (2) 4\n" +
			"(3) 7             does not trade\n" +
			"(3) 8             does not trade\n" +
			"\n" +
			"Num trades  = 4 of 8 items (50.0%)\n" +
			"Total cost  = 4 (avg 1.00)\n" +
			"Num groups  = 2\n" +
			"Group sizes = 2 2\n" +
			"Sum squares = 8\n";
	
	@Test
	public void transform() throws JsonProcessingException {
		List<TradeResultJson> tradeResults = TradeMaximizerTransformer.transform(TRADE_MAXIMIZER_RESULT);
		// Assertions
		assertEquals(8, tradeResults.size());
		// "(1) 1 receives (2) 3\n" +
		Link itemOne = ItemLinkAssember.buildLink(1, 1);
		// "(2) 3 receives (1) 1\n" +
		Link itemThree = ItemLinkAssember.buildLink(2, 3);
		// "(2) 4 receives (3) 6\n" +
		Link itemSix = ItemLinkAssember.buildLink(3, 6);
		// "(3) 6 receives (2) 4\n" +
		Link itemFour = ItemLinkAssember.buildLink(2, 4);
		// "(1) 2 does not trade\n" +
		Link itemTwo = ItemLinkAssember.buildLink(1, 2);
		//"(3) 7             does not trade\n" +
		Link itemSeven = ItemLinkAssember.buildLink(3, 7);
		
		boolean oneReceivesThree = tradeResults.removeIf(p 
				-> p.getOfferingItem().equals(itemOne) && p.getReceivingItem().equals(itemThree));
		assertTrue(oneReceivesThree);
		
		boolean sixReceivesFour = tradeResults.removeIf(p 
				-> p.getOfferingItem().equals(itemSix) && p.getReceivingItem().equals(itemFour));
		assertTrue(sixReceivesFour);

		boolean twoDoesNotTrade = tradeResults.removeIf(p 
				-> p.getOfferingItem().equals(itemTwo) && p.getReceivingItem() == null);
		assertTrue(twoDoesNotTrade);

		boolean sevenDoesNotTrade = tradeResults.removeIf(p 
				-> p.getOfferingItem().equals(itemSeven) && p.getReceivingItem() == null);
		assertTrue(sevenDoesNotTrade);

	}
	
}
