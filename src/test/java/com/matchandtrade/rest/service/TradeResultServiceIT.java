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
import com.matchandtrade.rest.service.TradeResultService;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.OfferRandom;
import com.matchandtrade.test.random.MembershipRandom;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;


@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeResultServiceIT {
	
	@Autowired
	private MembershipRandom membershipRandom;
	@Autowired
	private ArticleRandom articleRandom;
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
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		
		// Create owner's articles (Greek letters)
		MembershipEntity greekMembership = membershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("GREEK"), MembershipEntity.Type.MEMBER);
		ArticleEntity beta = articleRandom.nextPersistedEntity(greekMembership);
		
		// Create member's articles (country names)
		MembershipEntity countryMemberhip = membershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("COUNTRY"), MembershipEntity.Type.MEMBER);
		ArticleEntity brazil = articleRandom.nextPersistedEntity(countryMemberhip);

		// Create member's articles (ordinal numbers)
		MembershipEntity ordinalMemberhip = membershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("ORDINAL"), MembershipEntity.Type.MEMBER);
		ArticleEntity first = articleRandom.nextPersistedEntity(ordinalMemberhip);

		offerRandom.nextPersistedEntity(greekMembership.getMembershipId(), beta.getArticleId(), brazil.getArticleId());
		offerRandom.nextPersistedEntity(countryMemberhip.getMembershipId(), brazil.getArticleId(), first.getArticleId());
		offerRandom.nextPersistedEntity(ordinalMemberhip.getMembershipId(), first.getArticleId(), beta.getArticleId());
		
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
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());

		// Alice is also the trade owner
		MembershipEntity aliceMembership = membershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("alice"));
		ArticleEntity one = articleRandom.nextPersistedEntity(aliceMembership);
		MembershipEntity bettyMembership = membershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("betty"), MembershipEntity.Type.MEMBER);
		ArticleEntity two = articleRandom.nextPersistedEntity(bettyMembership);
		MembershipEntity craigMembership = membershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("craig"), MembershipEntity.Type.MEMBER);
		ArticleEntity three = articleRandom.nextPersistedEntity(craigMembership);
		MembershipEntity davidMembership = membershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("david"), MembershipEntity.Type.MEMBER);
		ArticleEntity four = articleRandom.nextPersistedEntity(davidMembership);
		MembershipEntity ethanMembership = membershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("ethan"), MembershipEntity.Type.MEMBER);
		ArticleEntity five = articleRandom.nextPersistedEntity(ethanMembership);
		MembershipEntity fionaMembership = membershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity("fiona"), MembershipEntity.Type.MEMBER);
		ArticleEntity six = articleRandom.nextPersistedEntity(fionaMembership);

		//(Alice) 1 : 3 2 6
		offerRandom.nextPersistedEntity(aliceMembership.getMembershipId(), one.getArticleId(), three.getArticleId());
		offerRandom.nextPersistedEntity(aliceMembership.getMembershipId(), one.getArticleId(), two.getArticleId());
		offerRandom.nextPersistedEntity(aliceMembership.getMembershipId(), one.getArticleId(), six.getArticleId());
		//(Betty) 2 : 1 6 4 3
		offerRandom.nextPersistedEntity(bettyMembership.getMembershipId(), two.getArticleId(), one.getArticleId());
		offerRandom.nextPersistedEntity(bettyMembership.getMembershipId(), two.getArticleId(), six.getArticleId());
		offerRandom.nextPersistedEntity(bettyMembership.getMembershipId(), two.getArticleId(), four.getArticleId());
		offerRandom.nextPersistedEntity(bettyMembership.getMembershipId(), two.getArticleId(), three.getArticleId());
		//(Craig) 3 : 6 2
		offerRandom.nextPersistedEntity(craigMembership.getMembershipId(), three.getArticleId(), six.getArticleId());
		offerRandom.nextPersistedEntity(craigMembership.getMembershipId(), three.getArticleId(), two.getArticleId());
		//(David) 4 : 2
		offerRandom.nextPersistedEntity(davidMembership.getMembershipId(), four.getArticleId(), two.getArticleId());
		//(Ethan) 5 : 1 2 3 4 6
		offerRandom.nextPersistedEntity(ethanMembership.getMembershipId(), five.getArticleId(), one.getArticleId());
		offerRandom.nextPersistedEntity(ethanMembership.getMembershipId(), five.getArticleId(), two.getArticleId());
		offerRandom.nextPersistedEntity(ethanMembership.getMembershipId(), five.getArticleId(), three.getArticleId());
		offerRandom.nextPersistedEntity(ethanMembership.getMembershipId(), five.getArticleId(), four.getArticleId());
		offerRandom.nextPersistedEntity(ethanMembership.getMembershipId(), five.getArticleId(), six.getArticleId());
		//(Fiona) 6 : 1 2
		offerRandom.nextPersistedEntity(fionaMembership.getMembershipId(), six.getArticleId(), one.getArticleId());
		offerRandom.nextPersistedEntity(fionaMembership.getMembershipId(), six.getArticleId(), two.getArticleId());

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
