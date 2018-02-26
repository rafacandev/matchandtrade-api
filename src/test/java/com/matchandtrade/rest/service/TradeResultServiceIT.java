package com.matchandtrade.rest.service;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.ItemEntity;
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
		ItemEntity beta = itemRandom.nextPersistedEntity(greekMembership);
		
		// Create member's items (country names)
		TradeMembershipEntity countryMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("COUNTRY"), TradeMembershipEntity.Type.MEMBER);
		ItemEntity brazil = itemRandom.nextPersistedEntity(countryMemberhip);

		// Create member's items (ordinal numbers)
		TradeMembershipEntity ordinalMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("ORDINAL"), TradeMembershipEntity.Type.MEMBER);
		ItemEntity first = itemRandom.nextPersistedEntity(ordinalMemberhip);

		offerRandom.nextPersistedEntity(greekMembership.getTradeMembershipId(), beta.getItemId(), brazil.getItemId());
		offerRandom.nextPersistedEntity(countryMemberhip.getTradeMembershipId(), brazil.getItemId(), first.getItemId());
		offerRandom.nextPersistedEntity(ordinalMemberhip.getTradeMembershipId(), first.getItemId(), beta.getItemId());
		
		// Generate the trade results
		trade.setState(TradeEntity.State.GENERATE_RESULTS);
		tradeRepositoryFacade.save(trade);
		String response = tradeResultService.buildTradeMaximizerOutput(trade.getTradeId());
		// Remove white spaces and tabs to facilitate assertion
		response = response.replace(" ", "").replaceAll("\t", "");
		
		String expectedBetaLine = "("+greekMembership.getTradeMembershipId()+")" + beta.getItemId() + "receives("+countryMemberhip.getTradeMembershipId()+")" + brazil.getItemId();
		assertTrue(response.contains(expectedBetaLine));
		String expectedBrazilLine = "("+countryMemberhip.getTradeMembershipId()+")" + brazil.getItemId() + "receives("+ordinalMemberhip.getTradeMembershipId()+")" + first.getItemId();
		assertTrue(response.contains(expectedBrazilLine));
		String expectedFirstLine = "("+ordinalMemberhip.getTradeMembershipId()+")" + first.getItemId() + "receives("+greekMembership.getTradeMembershipId()+")" + beta.getItemId();
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
		ItemEntity one = itemRandom.nextPersistedEntity(aliceMembership);
		TradeMembershipEntity bettyMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("betty"), TradeMembershipEntity.Type.MEMBER);
		ItemEntity two = itemRandom.nextPersistedEntity(bettyMembership);
		TradeMembershipEntity craigMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("craig"), TradeMembershipEntity.Type.MEMBER);
		ItemEntity three = itemRandom.nextPersistedEntity(craigMembership);
		TradeMembershipEntity davidMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("david"), TradeMembershipEntity.Type.MEMBER);
		ItemEntity four = itemRandom.nextPersistedEntity(davidMembership);
		TradeMembershipEntity ethanMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("ethan"), TradeMembershipEntity.Type.MEMBER);
		ItemEntity five = itemRandom.nextPersistedEntity(ethanMembership);
		TradeMembershipEntity fionaMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("fiona"), TradeMembershipEntity.Type.MEMBER);
		ItemEntity six = itemRandom.nextPersistedEntity(fionaMembership);

		//(Alice) 1 : 3 2 6
		offerRandom.nextPersistedEntity(aliceMembership.getTradeMembershipId(), one.getItemId(), three.getItemId());
		offerRandom.nextPersistedEntity(aliceMembership.getTradeMembershipId(), one.getItemId(), two.getItemId());
		offerRandom.nextPersistedEntity(aliceMembership.getTradeMembershipId(), one.getItemId(), six.getItemId());
		//(Betty) 2 : 1 6 4 3
		offerRandom.nextPersistedEntity(bettyMembership.getTradeMembershipId(), two.getItemId(), one.getItemId());
		offerRandom.nextPersistedEntity(bettyMembership.getTradeMembershipId(), two.getItemId(), six.getItemId());
		offerRandom.nextPersistedEntity(bettyMembership.getTradeMembershipId(), two.getItemId(), four.getItemId());
		offerRandom.nextPersistedEntity(bettyMembership.getTradeMembershipId(), two.getItemId(), three.getItemId());
		//(Craig) 3 : 6 2
		offerRandom.nextPersistedEntity(craigMembership.getTradeMembershipId(), three.getItemId(), six.getItemId());
		offerRandom.nextPersistedEntity(craigMembership.getTradeMembershipId(), three.getItemId(), two.getItemId());
		//(David) 4 : 2
		offerRandom.nextPersistedEntity(davidMembership.getTradeMembershipId(), four.getItemId(), two.getItemId());
		//(Ethan) 5 : 1 2 3 4 6
		offerRandom.nextPersistedEntity(ethanMembership.getTradeMembershipId(), five.getItemId(), one.getItemId());
		offerRandom.nextPersistedEntity(ethanMembership.getTradeMembershipId(), five.getItemId(), two.getItemId());
		offerRandom.nextPersistedEntity(ethanMembership.getTradeMembershipId(), five.getItemId(), three.getItemId());
		offerRandom.nextPersistedEntity(ethanMembership.getTradeMembershipId(), five.getItemId(), four.getItemId());
		offerRandom.nextPersistedEntity(ethanMembership.getTradeMembershipId(), five.getItemId(), six.getItemId());
		//(Fiona) 6 : 1 2
		offerRandom.nextPersistedEntity(fionaMembership.getTradeMembershipId(), six.getItemId(), one.getItemId());
		offerRandom.nextPersistedEntity(fionaMembership.getTradeMembershipId(), six.getItemId(), two.getItemId());

		// Generate the trade results
		trade.setState(TradeEntity.State.GENERATE_RESULTS);
		tradeRepositoryFacade.save(trade);
		String response = tradeResultService.buildTradeMaximizerOutput(trade.getTradeId());

		List<String> assertions = new ArrayList<>();
		assertions.add("("+aliceMembership.getTradeMembershipId()+")"+one.getItemId()+"receives("+craigMembership.getTradeMembershipId()+")"+three.getItemId());
		assertions.add("("+craigMembership.getTradeMembershipId()+")"+three.getItemId()+"receives("+fionaMembership.getTradeMembershipId()+")"+six.getItemId());
		assertions.add("("+fionaMembership.getTradeMembershipId()+")"+six.getItemId()+"receives("+aliceMembership.getTradeMembershipId()+")"+one.getItemId());
		assertions.add("("+bettyMembership.getTradeMembershipId()+")"+two.getItemId()+"receives("+davidMembership.getTradeMembershipId()+")"+four.getItemId());
		assertions.add("("+davidMembership.getTradeMembershipId()+")"+four.getItemId()+"receives("+bettyMembership.getTradeMembershipId()+")"+two.getItemId());
		
		// Remove white spaces and tabs to facilitate assertion
		response = response.replace(" ", "").replace("\t", "");
		
		for (String assertion : assertions) {
			assertTrue(response.contains(assertion));
		}
	}
}
