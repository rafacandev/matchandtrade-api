package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.OfferRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;


@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeResultControllerGetIT {
	
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeRandom tradeRandom;
	private TradeResultController fixture;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private OfferRandom offerRandom;
	@Autowired
	private UserRandom userRandom;
	@Autowired
	private TradeRepositoryFacade tradeRepositoryFacade;
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getTradeResultController(true);
		}
	}
	
	@Test(expected = RestException.class)
	public void resultsAreOnlyGeneratedIfTradeStatusIsMatchingItemsEndedOrGeneratingTradesEnded() throws IOException {
		UserEntity tradeOwner = fixture.authenticationProvider.getAuthentication().getUser();
		TradeEntity trade = tradeRandom.nextPersistedEntity(tradeOwner);
		try {
			fixture.get(trade.getTradeId());
		} catch (RestException e) {
			assertEquals("TradeResult is only availble when Trade.State is GENERATE_RESULTS, GENERATING_RESULTS, RESULTS_GENERATED.", e.getDescription());
			throw e;
		}
	}
	
	@Test
	public void csv() throws IOException {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity greekMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("GREEK"), TradeMembershipEntity.Type.MEMBER);
		ItemEntity alpha = itemRandom.nextPersistedEntity(greekMembership, "alpha");
		ItemEntity beta = itemRandom.nextPersistedEntity(greekMembership, "beta");
		
		// Create member's items (country names)
		TradeMembershipEntity countryMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("COUNTRY"), TradeMembershipEntity.Type.MEMBER);
		ItemEntity argentina = itemRandom.nextPersistedEntity(countryMemberhip, "argentina");
		ItemEntity brazil = itemRandom.nextPersistedEntity(countryMemberhip, "brazil");
		ItemEntity canada = itemRandom.nextPersistedEntity(countryMemberhip, "canada");

		// Create member's items (ordinal numbers)
		TradeMembershipEntity ordinalMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("ORDINAL"), TradeMembershipEntity.Type.MEMBER);
		ItemEntity first = itemRandom.nextPersistedEntity(ordinalMemberhip, "first");

		offerRandom.nextPersistedEntity(greekMembership.getTradeMembershipId(), alpha.getItemId(), canada.getItemId());
		offerRandom.nextPersistedEntity(greekMembership.getTradeMembershipId(), beta.getItemId(), argentina.getItemId());
		offerRandom.nextPersistedEntity(greekMembership.getTradeMembershipId(), beta.getItemId(), brazil.getItemId());
		offerRandom.nextPersistedEntity(countryMemberhip.getTradeMembershipId(), brazil.getItemId(), first.getItemId());
		offerRandom.nextPersistedEntity(countryMemberhip.getTradeMembershipId(), canada.getItemId(), alpha.getItemId());
		offerRandom.nextPersistedEntity(ordinalMemberhip.getTradeMembershipId(), first.getItemId(), beta.getItemId());
		
		// Generate the trade results
		trade.setState(TradeEntity.State.GENERATE_RESULTS);
		tradeRepositoryFacade.save(trade);
		String response = fixture.get(trade.getTradeId());
		// Remove white spaces and tabs to facilitate assertion
		response = response.replace(" ", "").replace("\t", "");
		
		String csvLine = greekMembership.getUser().getUserId() + "," + greekMembership.getUser().getName() + "," + alpha.getItemId() + "," + alpha.getName() + ",:RECEIVES:," +
				countryMemberhip.getUser().getUserId() + "," + countryMemberhip.getUser().getName() + "," + canada.getItemId() + "," + canada.getName() + ",:SENDS:," +
				countryMemberhip.getUser().getUserId() + "," + countryMemberhip.getUser().getName();
		assertTrue(response.contains(csvLine));
		
		csvLine = greekMembership.getUser().getUserId() + "," + greekMembership.getUser().getName() + "," + beta.getItemId() + "," + beta.getName() + ",:RECEIVES:," +
				countryMemberhip.getUser().getUserId() + "," + countryMemberhip.getUser().getName() + "," + brazil.getItemId() + "," + brazil.getName() + ",:SENDS:," +
				ordinalMemberhip.getUser().getUserId() + "," + ordinalMemberhip.getUser().getName();
		assertTrue(response.contains(csvLine));
	}

}
