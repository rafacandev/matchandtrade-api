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
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.helper.ArticleHelper;
import com.matchandtrade.test.helper.OfferHelper;
import com.matchandtrade.test.helper.MembershipHelper;
import com.matchandtrade.test.helper.TradeHelper;
import com.matchandtrade.test.helper.UserHelper;


@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeResultServiceIT {
	
	@Autowired
	private MembershipHelper membershipHelper;
	@Autowired
	private ArticleHelper articleHelper;
	@Autowired
	private OfferHelper offerHelper;
	@Autowired
	private TradeHelper tradeHelper;
	@Autowired
	private UserHelper userHelper;
	@Autowired
	private TradeRepositoryFacade tradeRepositoryFacade;
	@Autowired
	private TradeResultService tradeResultService;
	
	/**
	 *  Three way exchange where no articles are directly offered-wanted but can be indirectly exchanged.
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
	 *  EXPECTED RESULT: all articles should trade
	 *  </pre>
	 * 
	 * @throws IOException
	 */
	@Test
	public void shouldTradeThreeWayExchange() throws IOException {
		// Create a trade for a random user
		TradeEntity trade = tradeHelper.createPersistedEntity(userHelper.createPersistedEntity());
		
		// Create owner's articles (Greek letters)
		MembershipEntity greekMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity("GREEK"), trade);
		ArticleEntity beta = articleHelper.createPersistedEntity(greekMembership);
		
		// Create member's articles (country names)
		MembershipEntity countryMemberhip = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity("COUNTRY"), trade);
		ArticleEntity brazil = articleHelper.createPersistedEntity(countryMemberhip);

		// Create member's articles (ordinal numbers)
		MembershipEntity ordinalMemberhip = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity("ORDINAL"), trade);
		ArticleEntity first = articleHelper.createPersistedEntity(ordinalMemberhip);

		offerHelper.createPersistedEntity(greekMembership.getMembershipId(), beta.getArticleId(), brazil.getArticleId());
		offerHelper.createPersistedEntity(countryMemberhip.getMembershipId(), brazil.getArticleId(), first.getArticleId());
		offerHelper.createPersistedEntity(ordinalMemberhip.getMembershipId(), first.getArticleId(), beta.getArticleId());
		
		// Generate the trade results
		trade.setState(TradeEntity.State.GENERATE_RESULTS);
		tradeRepositoryFacade.save(trade);
		String response = tradeResultService.buildTradeMaximizerOutput(trade.getTradeId());
		// Remove white spaces and tabs to facilitate assertion
		response = response.replace(" ", "").replaceAll("\t", "");
		
		String expectedBetaLine = "("+greekMembership.getMembershipId()+")" + beta.getArticleId() + "receives("+countryMemberhip.getMembershipId()+")" + brazil.getArticleId();
		assertTrue(response.contains(expectedBetaLine));
		String expectedBrazilLine = "("+countryMemberhip.getMembershipId()+")" + brazil.getArticleId() + "receives("+ordinalMemberhip.getMembershipId()+")" + first.getArticleId();
		assertTrue(response.contains(expectedBrazilLine));
		String expectedFirstLine = "("+ordinalMemberhip.getMembershipId()+")" + first.getArticleId() + "receives("+greekMembership.getMembershipId()+")" + beta.getArticleId();
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
		TradeEntity trade = tradeHelper.createPersistedEntity(userHelper.createPersistedEntity());

		// Alice is also the trade owner
		MembershipEntity aliceMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity("alice"), trade);
		ArticleEntity one = articleHelper.createPersistedEntity(aliceMembership);
		MembershipEntity bettyMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity("betty"), trade);
		ArticleEntity two = articleHelper.createPersistedEntity(bettyMembership);
		MembershipEntity craigMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity("craig"), trade);
		ArticleEntity three = articleHelper.createPersistedEntity(craigMembership);
		MembershipEntity davidMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity("david"), trade);
		ArticleEntity four = articleHelper.createPersistedEntity(davidMembership);
		MembershipEntity ericMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity("eric"), trade);
		ArticleEntity five = articleHelper.createPersistedEntity(ericMembership);
		MembershipEntity fionaMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity("fiona"), trade);
		ArticleEntity six = articleHelper.createPersistedEntity(fionaMembership);

		//(Alice) 1 : 3 2 6
		offerHelper.createPersistedEntity(aliceMembership.getMembershipId(), one.getArticleId(), three.getArticleId());
		offerHelper.createPersistedEntity(aliceMembership.getMembershipId(), one.getArticleId(), two.getArticleId());
		offerHelper.createPersistedEntity(aliceMembership.getMembershipId(), one.getArticleId(), six.getArticleId());
		//(Betty) 2 : 1 6 4 3
		offerHelper.createPersistedEntity(bettyMembership.getMembershipId(), two.getArticleId(), one.getArticleId());
		offerHelper.createPersistedEntity(bettyMembership.getMembershipId(), two.getArticleId(), six.getArticleId());
		offerHelper.createPersistedEntity(bettyMembership.getMembershipId(), two.getArticleId(), four.getArticleId());
		offerHelper.createPersistedEntity(bettyMembership.getMembershipId(), two.getArticleId(), three.getArticleId());
		//(Craig) 3 : 6 2
		offerHelper.createPersistedEntity(craigMembership.getMembershipId(), three.getArticleId(), six.getArticleId());
		offerHelper.createPersistedEntity(craigMembership.getMembershipId(), three.getArticleId(), two.getArticleId());
		//(David) 4 : 2
		offerHelper.createPersistedEntity(davidMembership.getMembershipId(), four.getArticleId(), two.getArticleId());
		//(Ethan) 5 : 1 2 3 4 6
		offerHelper.createPersistedEntity(ericMembership.getMembershipId(), five.getArticleId(), one.getArticleId());
		offerHelper.createPersistedEntity(ericMembership.getMembershipId(), five.getArticleId(), two.getArticleId());
		offerHelper.createPersistedEntity(ericMembership.getMembershipId(), five.getArticleId(), three.getArticleId());
		offerHelper.createPersistedEntity(ericMembership.getMembershipId(), five.getArticleId(), four.getArticleId());
		offerHelper.createPersistedEntity(ericMembership.getMembershipId(), five.getArticleId(), six.getArticleId());
		//(Fiona) 6 : 1 2
		offerHelper.createPersistedEntity(fionaMembership.getMembershipId(), six.getArticleId(), one.getArticleId());
		offerHelper.createPersistedEntity(fionaMembership.getMembershipId(), six.getArticleId(), two.getArticleId());

		// Generate the trade results
		trade.setState(TradeEntity.State.GENERATE_RESULTS);
		tradeRepositoryFacade.save(trade);
		String response = tradeResultService.buildTradeMaximizerOutput(trade.getTradeId());

		List<String> assertions = new ArrayList<>();
		assertions.add("("+aliceMembership.getMembershipId()+")"+one.getArticleId()+"receives("+craigMembership.getMembershipId()+")"+three.getArticleId());
		assertions.add("("+craigMembership.getMembershipId()+")"+three.getArticleId()+"receives("+fionaMembership.getMembershipId()+")"+six.getArticleId());
		assertions.add("("+fionaMembership.getMembershipId()+")"+six.getArticleId()+"receives("+aliceMembership.getMembershipId()+")"+one.getArticleId());
		assertions.add("("+bettyMembership.getMembershipId()+")"+two.getArticleId()+"receives("+davidMembership.getMembershipId()+")"+four.getArticleId());
		assertions.add("("+davidMembership.getMembershipId()+")"+four.getArticleId()+"receives("+bettyMembership.getMembershipId()+")"+two.getArticleId());
		
		// Remove white spaces and tabs to facilitate assertion
		response = response.replace(" ", "").replace("\t", "");
		
		for (String assertion : assertions) {
			assertTrue(response.contains(assertion));
		}
	}
}
