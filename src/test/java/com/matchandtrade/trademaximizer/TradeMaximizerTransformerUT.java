package com.matchandtrade.trademaximizer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

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
			"(1) 1 receives (2) 3 and sends to (2) 3\n" +
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
	public void transform() {
		TradeMaximizerTransformer.transform(TRADE_MAXIMIZER_RESULT);

	}
	
}
