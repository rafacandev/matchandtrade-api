package com.matchandtrade.rest.service;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.matchandtrade.rest.service.TradeResultService;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.OfferRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;


@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeResultServiceIT {
	
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private OfferRandom offerRandom;
	@Autowired
	private TradeRandom tradeRandom;
	@Autowired
	private UserRandom userRandom;
	@Autowired
	private TradeRepositoryFacade tradeRepositoryFacade;
	@Autowired
	private TradeResultService tradeResultService;
	
	/**
	 *  Three way exchange where no items are directly offered-wanted but can be indirectly exchanged.
	 *  
	 *  <pre>
	 *  INPUT
	 *  ====================
	 *  OFFERED   :  WANTED
	 *  --------------------
	 *  beta      : brazil
	 *  brazil    : first,
	 *  first     : beta
	 *  --------------------
	 *  
	 *  EXPECTED RESULT: all items should trade
	 *  </pre>
	 * 
	 * @throws IOException
	 */
	@Test
	public void shouldTradeThreeWayExchange() throws IOException {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity greekMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("GREEK"), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity beta = itemRandom.nextPersistedEntity(greekMembership);
		
		// Create member's items (country names)
		TradeMembershipEntity countryMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("COUNTRY"), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity brazil = itemRandom.nextPersistedEntity(countryMemberhip);

		// Create member's items (ordinal numbers)
		TradeMembershipEntity ordinalMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("ORDINAL"), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity first = itemRandom.nextPersistedEntity(ordinalMemberhip);

		offerRandom.nextPersistedEntity(greekMembership.getTradeMembershipId(), beta.getArticleId(), brazil.getArticleId());
		offerRandom.nextPersistedEntity(countryMemberhip.getTradeMembershipId(), brazil.getArticleId(), first.getArticleId());
		offerRandom.nextPersistedEntity(ordinalMemberhip.getTradeMembershipId(), first.getArticleId(), beta.getArticleId());
		
		// Generate the trade results
		trade.setState(TradeEntity.State.GENERATE_RESULTS);
		tradeRepositoryFacade.save(trade);
		String response = tradeResultService.buildTradeMaximizerOutput(trade.getTradeId());
		// Remove white spaces and tabs to facilitate assertion
		response = response.replace(" ", "").replaceAll("\t", "");
		
		String expectedBetaLine = "("+greekMembership.getTradeMembershipId()+")" + beta.getArticleId() + "receives("+countryMemberhip.getTradeMembershipId()+")" + brazil.getArticleId();
		assertTrue(response.contains(expectedBetaLine));
		String expectedBrazilLine = "("+countryMemberhip.getTradeMembershipId()+")" + brazil.getArticleId() + "receives("+ordinalMemberhip.getTradeMembershipId()+")" + first.getArticleId();
		assertTrue(response.contains(expectedBrazilLine));
		String expectedFirstLine = "("+ordinalMemberhip.getTradeMembershipId()+")" + first.getArticleId() + "receives("+greekMembership.getTradeMembershipId()+")" + beta.getArticleId();
		assertTrue(response.contains(expectedFirstLine));
	}
	
	/**
	 * Reproducing the example from TradeMaximizer website: https://github.com/chrisokasaki/TradeMaximizer
	<pre>
		INPUT
		======================
		(Alice) 1 : 3 2 6
		(Betty) 2 : 1 6 4 3
		(Craig) 3 : 6 2
		(David) 4 : 2
		(Ethan) 5 : 1 2 3 4 6
		(Fiona) 6 : 1 2
		
		EXPECTED RESULT
		======================
		(ALICE) 1 receives (CRAIG) 3
		(CRAIG) 3 receives (FIONA) 6
		(FIONA) 6 receives (ALICE) 1
		
		(BETTY) 2 receives (DAVID) 4
		(DAVID) 4 receives (BETTY) 2
	</pre>
	 * @throws IOException 
	 */
	@Test
	public void shouldTradeAsInTradeMaximizerWebsiteExample() throws IOException {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());

		// Alice is also the trade owner
		TradeMembershipEntity aliceMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("alice"));
		ArticleEntity one = itemRandom.nextPersistedEntity(aliceMembership);
		TradeMembershipEntity bettyMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("betty"), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity two = itemRandom.nextPersistedEntity(bettyMembership);
		TradeMembershipEntity craigMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("craig"), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity three = itemRandom.nextPersistedEntity(craigMembership);
		TradeMembershipEntity davidMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("david"), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity four = itemRandom.nextPersistedEntity(davidMembership);
		TradeMembershipEntity ethanMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("ethan"), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity five = itemRandom.nextPersistedEntity(ethanMembership);
		TradeMembershipEntity fionaMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("fiona"), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity six = itemRandom.nextPersistedEntity(fionaMembership);

		//(Alice) 1 : 3 2 6
		offerRandom.nextPersistedEntity(aliceMembership.getTradeMembershipId(), one.getArticleId(), three.getArticleId());
		offerRandom.nextPersistedEntity(aliceMembership.getTradeMembershipId(), one.getArticleId(), two.getArticleId());
		offerRandom.nextPersistedEntity(aliceMembership.getTradeMembershipId(), one.getArticleId(), six.getArticleId());
		//(Betty) 2 : 1 6 4 3
		offerRandom.nextPersistedEntity(bettyMembership.getTradeMembershipId(), two.getArticleId(), one.getArticleId());
		offerRandom.nextPersistedEntity(bettyMembership.getTradeMembershipId(), two.getArticleId(), six.getArticleId());
		offerRandom.nextPersistedEntity(bettyMembership.getTradeMembershipId(), two.getArticleId(), four.getArticleId());
		offerRandom.nextPersistedEntity(bettyMembership.getTradeMembershipId(), two.getArticleId(), three.getArticleId());
		//(Craig) 3 : 6 2
		offerRandom.nextPersistedEntity(craigMembership.getTradeMembershipId(), three.getArticleId(), six.getArticleId());
		offerRandom.nextPersistedEntity(craigMembership.getTradeMembershipId(), three.getArticleId(), two.getArticleId());
		//(David) 4 : 2
		offerRandom.nextPersistedEntity(davidMembership.getTradeMembershipId(), four.getArticleId(), two.getArticleId());
		//(Ethan) 5 : 1 2 3 4 6
		offerRandom.nextPersistedEntity(ethanMembership.getTradeMembershipId(), five.getArticleId(), one.getArticleId());
		offerRandom.nextPersistedEntity(ethanMembership.getTradeMembershipId(), five.getArticleId(), two.getArticleId());
		offerRandom.nextPersistedEntity(ethanMembership.getTradeMembershipId(), five.getArticleId(), three.getArticleId());
		offerRandom.nextPersistedEntity(ethanMembership.getTradeMembershipId(), five.getArticleId(), four.getArticleId());
		offerRandom.nextPersistedEntity(ethanMembership.getTradeMembershipId(), five.getArticleId(), six.getArticleId());
		//(Fiona) 6 : 1 2
		offerRandom.nextPersistedEntity(fionaMembership.getTradeMembershipId(), six.getArticleId(), one.getArticleId());
		offerRandom.nextPersistedEntity(fionaMembership.getTradeMembershipId(), six.getArticleId(), two.getArticleId());

		// Generate the trade results
		trade.setState(TradeEntity.State.GENERATE_RESULTS);
		tradeRepositoryFacade.save(trade);
		String response = tradeResultService.buildTradeMaximizerOutput(trade.getTradeId());

		List<String> assertions = new ArrayList<>();
		assertions.add("("+aliceMembership.getTradeMembershipId()+")"+one.getArticleId()+"receives("+craigMembership.getTradeMembershipId()+")"+three.getArticleId());
		assertions.add("("+craigMembership.getTradeMembershipId()+")"+three.getArticleId()+"receives("+fionaMembership.getTradeMembershipId()+")"+six.getArticleId());
		assertions.add("("+fionaMembership.getTradeMembershipId()+")"+six.getArticleId()+"receives("+aliceMembership.getTradeMembershipId()+")"+one.getArticleId());
		assertions.add("("+bettyMembership.getTradeMembershipId()+")"+two.getArticleId()+"receives("+davidMembership.getTradeMembershipId()+")"+four.getArticleId());
		assertions.add("("+davidMembership.getTradeMembershipId()+")"+four.getArticleId()+"receives("+bettyMembership.getTradeMembershipId()+")"+two.getArticleId());
		
		// Remove white spaces and tabs to facilitate assertion
		response = response.replace(" ", "").replace("\t", "");
		
		for (String assertion : assertions) {
			assertTrue(response.contains(assertion));
		}
	}
}
