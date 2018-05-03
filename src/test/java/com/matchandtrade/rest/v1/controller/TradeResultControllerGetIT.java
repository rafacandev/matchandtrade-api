package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.TradeService;
import com.matchandtrade.rest.v1.json.TradeResultJson;
import com.matchandtrade.rest.v1.json.TradedItemJson;
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
	private TradeService tradeService;
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getTradeResultController(true);
		}
	}
	
	@Test(expected = RestException.class)
	public void shouldNotGenerateResultsWhenStatusIsSubmittingItems() {
		UserEntity tradeOwner = fixture.authenticationProvider.getAuthentication().getUser();
		TradeEntity trade = tradeRandom.nextPersistedEntity(tradeOwner);
		try {
			fixture.getJson(trade.getTradeId());
		} catch (RestException e) {
			assertEquals("TradeResult is only availble when Trade.State is RESULTS_GENERATED.", e.getDescription());
			throw e;
		}
	}

	@Test
	public void shouldGenerateResultsWhenStatusResultsGenerated() {
		UserEntity tradeOwner = fixture.authenticationProvider.getAuthentication().getUser();
		TradeEntity trade = tradeRandom.nextPersistedEntity(tradeOwner);
		trade.setState(TradeEntity.State.GENERATE_RESULTS);
		tradeService.update(trade);
		TradeResultJson response = fixture.getJson(trade.getTradeId());
		assertNotNull(response);
	}
	
	@Test
	public void shouldGenerateCsvResults() {
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
		tradeService.update(trade);
		String response = fixture.getCsv(trade.getTradeId());
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
	
	@Test
	public void shouldGenerateJsonResult() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity greekMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("GREEK"), TradeMembershipEntity.Type.MEMBER);
		ItemEntity alpha = itemRandom.nextPersistedEntity(greekMembership, "alpha");
		ItemEntity beta = itemRandom.nextPersistedEntity(greekMembership, "beta");
		
		// Create member's items (country names)
		TradeMembershipEntity countryMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("COUNTRY"), TradeMembershipEntity.Type.MEMBER);
		ItemEntity argentina = itemRandom.nextPersistedEntity(countryMembership, "argentina");
		ItemEntity brazil = itemRandom.nextPersistedEntity(countryMembership, "brazil");
		ItemEntity canada = itemRandom.nextPersistedEntity(countryMembership, "canada");

		// Create member's items (ordinal numbers)
		TradeMembershipEntity ordinalMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("ORDINAL"), TradeMembershipEntity.Type.MEMBER);
		ItemEntity first = itemRandom.nextPersistedEntity(ordinalMembership, "first");

		offerRandom.nextPersistedEntity(greekMembership.getTradeMembershipId(), alpha.getItemId(), canada.getItemId());
		offerRandom.nextPersistedEntity(greekMembership.getTradeMembershipId(), beta.getItemId(), argentina.getItemId());
		offerRandom.nextPersistedEntity(greekMembership.getTradeMembershipId(), beta.getItemId(), brazil.getItemId());
		offerRandom.nextPersistedEntity(countryMembership.getTradeMembershipId(), brazil.getItemId(), first.getItemId());
		offerRandom.nextPersistedEntity(countryMembership.getTradeMembershipId(), canada.getItemId(), alpha.getItemId());
		offerRandom.nextPersistedEntity(ordinalMembership.getTradeMembershipId(), first.getItemId(), beta.getItemId());
		
		// Generate the trade results
		trade.setState(TradeEntity.State.GENERATE_RESULTS);
		tradeService.update(trade);
		TradeResultJson response = fixture.getJson(trade.getTradeId());
		
		assertEquals(trade.getTradeId(), response.getTradeId());
		assertEquals(trade.getName(), response.getTradeName());
		assertEquals(new Integer(6), response.getTotalOfItems());
		assertEquals(new Integer(1), response.getTotalOfNotTradedItems());
		assertEquals(new Integer(5), response.getTotalOfTradedItems());
		
		// 1100,GREEK,1005,alpha,:RECEIVES:,1101,COUNTRY,1009,canada,:SENDS:,1101,COUNTRY
		boolean greekOfferedAlphaReceivesCanadaSendsToCountry = false;
		// 1420,GREEK,1326,beta,:RECEIVES:,1421,COUNTRY,1328,brazil,:SENDS:,1422,ORDINAL
		boolean greekOfferedBetaReceivesBrazilSendsToOrdinal = false;
		// 1421,COUNTRY,1328,brazil,:RECEIVES:,1422,ORDINAL,1330,first,:SENDS:,1420,GREEK
		boolean countryOfferedBrazilRecivesFirstSendsToGreek = false;
		// 1421,COUNTRY,1329,canada,:RECEIVES:,1420,GREEK,1325,alpha,:SENDS:,1420,GREEK
		boolean countryOfferedCanadaReceivesAlphaSendsToGreek = false;
		// 1422,ORDINAL,1330,first,:RECEIVES:,1420,GREEK,1326,beta,:SENDS:,1421,COUNTRY
		boolean ordinalOfferedFirstReceivesBetaSendsToCountry = false;
		boolean foundArgentinaInTradeResults = false;
		for(TradedItemJson tradedItem : response.getTradedItems()) {
			if (greekMembership.getUser().getUserId().equals(tradedItem.getUserId())
					&& alpha.getItemId().equals(tradedItem.getItemId())
					&& countryMembership.getUser().getUserId().equals(tradedItem.getReceivingUserId())
					&& canada.getItemId().equals(tradedItem.getReceivingItemId())
					&& countryMembership.getUser().getUserId().equals(tradedItem.getSendingUserId())) {
				greekOfferedAlphaReceivesCanadaSendsToCountry = true;
				continue;
			}
			if (greekMembership.getUser().getUserId().equals(tradedItem.getUserId())
					&& beta.getItemId().equals(tradedItem.getItemId())
					&& countryMembership.getUser().getUserId().equals(tradedItem.getReceivingUserId())
					&& brazil.getItemId().equals(tradedItem.getReceivingItemId())
					&& ordinalMembership.getUser().getUserId().equals(tradedItem.getSendingUserId())) {
				greekOfferedBetaReceivesBrazilSendsToOrdinal = true;
				continue;
			}
			if (countryMembership.getUser().getUserId().equals(tradedItem.getUserId())
					&& brazil.getItemId().equals(tradedItem.getItemId())
					&& ordinalMembership.getUser().getUserId().equals(tradedItem.getReceivingUserId())
					&& first.getItemId().equals(tradedItem.getReceivingItemId())
					&& greekMembership.getUser().getUserId().equals(tradedItem.getSendingUserId())) {
				countryOfferedBrazilRecivesFirstSendsToGreek = true;
				continue;
			}
			if (countryMembership.getUser().getUserId().equals(tradedItem.getUserId())
					&& canada.getItemId().equals(tradedItem.getItemId())
					&& greekMembership.getUser().getUserId().equals(tradedItem.getReceivingUserId())
					&& alpha.getItemId().equals(tradedItem.getReceivingItemId())
					&& greekMembership.getUser().getUserId().equals(tradedItem.getSendingUserId())) {
				countryOfferedCanadaReceivesAlphaSendsToGreek = true;
				continue;
			}
			if (ordinalMembership.getUser().getUserId().equals(tradedItem.getUserId())
					&& first.getItemId().equals(tradedItem.getItemId())
					&& greekMembership.getUser().getUserId().equals(tradedItem.getReceivingUserId())
					&& beta.getItemId().equals(tradedItem.getReceivingItemId())
					&& countryMembership.getUser().getUserId().equals(tradedItem.getSendingUserId())) {
				ordinalOfferedFirstReceivesBetaSendsToCountry = true;
				continue;
			}
			if (countryMembership.getUser().getUserId().equals(tradedItem.getUserId())
					&& argentina.getItemId().equals(tradedItem.getItemId())) {
				foundArgentinaInTradeResults = true;
			}
			
		}
		assertTrue(greekOfferedAlphaReceivesCanadaSendsToCountry);
		assertTrue(greekOfferedBetaReceivesBrazilSendsToOrdinal);
		assertTrue(countryOfferedBrazilRecivesFirstSendsToGreek);
		assertTrue(countryOfferedCanadaReceivesAlphaSendsToGreek);
		assertTrue(ordinalOfferedFirstReceivesBetaSendsToCountry);
		assertFalse(foundArgentinaInTradeResults);
	}

	@Test
	public void shouldGetJsonResultsMultipleTimes() {
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		trade.setState(TradeEntity.State.GENERATE_RESULTS);
		tradeService.update(trade);
		TradeResultJson response1 = fixture.getJson(trade.getTradeId());
		assertEquals(new Integer(trade.getTradeId()), response1.getTradeId());
		TradeResultJson response2 = fixture.getJson(trade.getTradeId());
		assertEquals(new Integer(trade.getTradeId()), response2.getTradeId());
		TradeResultJson response3 = fixture.getJson(trade.getTradeId());
		assertEquals(new Integer(trade.getTradeId()), response3.getTradeId());
	}
	
	@Test(expected=RestException.class)
	public void shouldThrowErrorWhenResultsAreNotGenerated() {
		// Very rare case where the State.RESULTS_GENERATED but for some reason the results
		// were not generated (prossibly due to error when generating the results)
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		trade.setState(TradeEntity.State.RESULTS_GENERATED);
		tradeService.update(trade);
		fixture.getJson(trade.getTradeId());
	}

	@Test(expected=RestException.class)
	public void shouldThrowErrorWhenGettingResultsForTradeStateSubmittingItems() {
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		trade.setState(TradeEntity.State.SUBMITTING_ITEMS);
		tradeService.update(trade);
		fixture.getJson(trade.getTradeId());
	}

}
