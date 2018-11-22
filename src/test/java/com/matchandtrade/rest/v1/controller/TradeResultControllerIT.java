package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.service.TradeService;
import com.matchandtrade.rest.v1.json.TradeResultJson;
import com.matchandtrade.rest.v1.json.TradedArticleJson;
import com.matchandtrade.test.helper.*;
import com.matchandtrade.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "file:config/matchandtrade.properties")
@SpringBootTest
@WebAppConfiguration
public class TradeResultControllerIT {

	@Autowired
	private ArticleHelper articleHelper;
	private String authorizationHeader;
	@Autowired
	private ControllerHelper controllerHelper;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private MembershipHelper membershipHelper;
	private MockMvc mockMvc;
	@Autowired
	private OfferHelper offerHelper;
	@Autowired
	private TradeHelper tradeHelper;
	@Autowired
	private TradeService tradeService;
	private UserEntity user;
	@Autowired
	private UserHelper userHelper;

	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		// Reusing user and authorization header for better performance
		if (user == null) {
			user = userHelper.createPersistedEntity();
			authorizationHeader = controllerHelper.generateAuthorizationHeader(user);
		}
	}

	public class TradeResultFactory {
		public TradeEntity trade;
		public MembershipEntity greekMembership;
		public ArticleEntity alpha;
		public ArticleEntity beta;
		public MembershipEntity countryMembership;
		public ArticleEntity argentina;
		public ArticleEntity brazil;
		public ArticleEntity canada;
		public MembershipEntity ordinalMembership;
		public ArticleEntity first;
		public TradeResultJson tradeResult;
		public String responseText;

		public TradeResultFactory make(
					UserEntity user,
					UserHelper userHelper,
					TradeHelper tradeHelper,
					TradeService tradeService,
					MembershipHelper membershipHelper,
					ArticleHelper articleHelper,
					OfferHelper offerHelper,
					MockMvc mockMvc,
					String contentType) throws Exception {
			TradeResultFactory result = new TradeResultFactory();

			// Create a trade for a random user
			result.trade = tradeHelper.createPersistedEntity(user);

			// Create owner's articles (Greek letters)
			result.greekMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity("GREEK"), result.trade);
			result.alpha = articleHelper.createPersistedEntity(result.greekMembership, "alpha");
			result.beta = articleHelper.createPersistedEntity(result.greekMembership, "beta");

			// Create member's articles (country names)
			result.countryMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity("COUNTRY"), result.trade);
			result.argentina = articleHelper.createPersistedEntity(result.countryMembership, "argentina");
			result.brazil = articleHelper.createPersistedEntity(result.countryMembership, "brazil");
			result.canada = articleHelper.createPersistedEntity(result.countryMembership, "canada");

			// Create member's articles (ordinal numbers)
			result.ordinalMembership = membershipHelper.subscribeUserToTrade(userHelper.createPersistedEntity("ORDINAL"), result.trade);
			result.first = articleHelper.createPersistedEntity(result.ordinalMembership, "first");

			offerHelper.createPersistedEntity(result.greekMembership.getMembershipId(), result.alpha.getArticleId(), result.canada.getArticleId());
			offerHelper.createPersistedEntity(result.greekMembership.getMembershipId(), result.beta.getArticleId(), result.argentina.getArticleId());
			offerHelper.createPersistedEntity(result.greekMembership.getMembershipId(), result.beta.getArticleId(), result.brazil.getArticleId());
			offerHelper.createPersistedEntity(result.countryMembership.getMembershipId(), result.brazil.getArticleId(), result.first.getArticleId());
			offerHelper.createPersistedEntity(result.countryMembership.getMembershipId(), result.canada.getArticleId(), result.alpha.getArticleId());
			offerHelper.createPersistedEntity(result.ordinalMembership.getMembershipId(), result.first.getArticleId(), result.beta.getArticleId());

			// Generate the trade results
			result.trade.setState(TradeEntity.State.GENERATE_RESULTS);
			tradeService.update(result.trade);

			MockHttpServletResponse response = mockMvc.perform(
				get("/matchandtrade-api/v1/trades/{tradeId}/results", result.trade.getTradeId())
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(contentType)
				)
				.andExpect(status().isOk())
				.andReturn()
				.getResponse();

			result.responseText = response.getContentAsString();
			return result;
		}
	}

	@Test
	public void get_When_ResultsAreGeneratedInCsvFormat() throws Exception {
		TradeResultFactory tradeResultFactory = new TradeResultFactory()
			.make(user, userHelper, tradeHelper, tradeService, membershipHelper, articleHelper, offerHelper, mockMvc, "text/csv");
		MembershipEntity greekMembership = tradeResultFactory.greekMembership;
		ArticleEntity alpha = tradeResultFactory.alpha;
		ArticleEntity beta = tradeResultFactory.beta;
		MembershipEntity countryMembership = tradeResultFactory.countryMembership;
		ArticleEntity brazil = tradeResultFactory.brazil;
		ArticleEntity canada = tradeResultFactory.canada;
		MembershipEntity ordinalMembership = tradeResultFactory.ordinalMembership;

		String response = tradeResultFactory.responseText;

		// Remove white spaces and tabs to facilitate assertion
		response = response.replace(" ", "").replace("\t", "");

		String csvLine = greekMembership.getUser().getUserId() + "," + greekMembership.getUser().getName() + "," + alpha.getArticleId() + "," + alpha.getName() + ",:RECEIVES:," +
			countryMembership.getUser().getUserId() + "," + countryMembership.getUser().getName() + "," + canada.getArticleId() + "," + canada.getName() + ",:SENDS:," +
			countryMembership.getUser().getUserId() + "," + countryMembership.getUser().getName();
		assertTrue(response.contains(csvLine));

		csvLine = greekMembership.getUser().getUserId() + "," + greekMembership.getUser().getName() + "," + beta.getArticleId() + "," + beta.getName() + ",:RECEIVES:," +
			countryMembership.getUser().getUserId() + "," + countryMembership.getUser().getName() + "," + brazil.getArticleId() + "," + brazil.getName() + ",:SENDS:," +
			ordinalMembership.getUser().getUserId() + "," + ordinalMembership.getUser().getName();
		assertTrue(response.contains(csvLine));
	}

	@Test
	public void get_When_ResultsAreGeneratedInJsonFormat_Then_Suceeds() throws Exception {
		TradeResultFactory tradeResultFactory = new TradeResultFactory()
			.make(user, userHelper, tradeHelper, tradeService, membershipHelper, articleHelper, offerHelper, mockMvc, MediaType.APPLICATION_JSON.toString());

		TradeEntity trade = tradeResultFactory.trade;
		MembershipEntity greekMembership = tradeResultFactory.greekMembership;
		ArticleEntity alpha = tradeResultFactory.alpha;
		ArticleEntity beta = tradeResultFactory.beta;
		MembershipEntity countryMembership = tradeResultFactory.countryMembership;
		ArticleEntity argentina = tradeResultFactory.argentina;
		ArticleEntity brazil = tradeResultFactory.brazil;
		ArticleEntity canada = tradeResultFactory.canada;
		MembershipEntity ordinalMembership = tradeResultFactory.ordinalMembership;
		ArticleEntity first = tradeResultFactory.first;
		TradeResultJson tradeResult = JsonUtil.fromString(tradeResultFactory.responseText, TradeResultJson.class);

		assertEquals(trade.getTradeId(), tradeResult.getTradeId());
		assertEquals(trade.getName(), tradeResult.getTradeName());
		assertEquals(new Integer(6), tradeResult.getTotalOfArticles());
		assertEquals(new Integer(1), tradeResult.getTotalOfNotTradedArticles());
		assertEquals(new Integer(5), tradeResult.getTotalOfTradedArticles());

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
		for(TradedArticleJson tradedArticle : tradeResult.getTradedArticles()) {
			if (greekMembership.getUser().getUserId().equals(tradedArticle.getUserId())
					&& alpha.getArticleId().equals(tradedArticle.getArticleId())
					&& countryMembership.getUser().getUserId().equals(tradedArticle.getReceivingUserId())
					&& canada.getArticleId().equals(tradedArticle.getReceivingArticleId())
					&& countryMembership.getUser().getUserId().equals(tradedArticle.getSendingUserId())) {
				greekOfferedAlphaReceivesCanadaSendsToCountry = true;
				continue;
			}
			if (greekMembership.getUser().getUserId().equals(tradedArticle.getUserId())
					&& beta.getArticleId().equals(tradedArticle.getArticleId())
					&& countryMembership.getUser().getUserId().equals(tradedArticle.getReceivingUserId())
					&& brazil.getArticleId().equals(tradedArticle.getReceivingArticleId())
					&& ordinalMembership.getUser().getUserId().equals(tradedArticle.getSendingUserId())) {
				greekOfferedBetaReceivesBrazilSendsToOrdinal = true;
				continue;
			}
			if (countryMembership.getUser().getUserId().equals(tradedArticle.getUserId())
					&& brazil.getArticleId().equals(tradedArticle.getArticleId())
					&& ordinalMembership.getUser().getUserId().equals(tradedArticle.getReceivingUserId())
					&& first.getArticleId().equals(tradedArticle.getReceivingArticleId())
					&& greekMembership.getUser().getUserId().equals(tradedArticle.getSendingUserId())) {
				countryOfferedBrazilRecivesFirstSendsToGreek = true;
				continue;
			}
			if (countryMembership.getUser().getUserId().equals(tradedArticle.getUserId())
					&& canada.getArticleId().equals(tradedArticle.getArticleId())
					&& greekMembership.getUser().getUserId().equals(tradedArticle.getReceivingUserId())
					&& alpha.getArticleId().equals(tradedArticle.getReceivingArticleId())
					&& greekMembership.getUser().getUserId().equals(tradedArticle.getSendingUserId())) {
				countryOfferedCanadaReceivesAlphaSendsToGreek = true;
				continue;
			}
			if (ordinalMembership.getUser().getUserId().equals(tradedArticle.getUserId())
					&& first.getArticleId().equals(tradedArticle.getArticleId())
					&& greekMembership.getUser().getUserId().equals(tradedArticle.getReceivingUserId())
					&& beta.getArticleId().equals(tradedArticle.getReceivingArticleId())
					&& countryMembership.getUser().getUserId().equals(tradedArticle.getSendingUserId())) {
				ordinalOfferedFirstReceivesBetaSendsToCountry = true;
				continue;
			}
			if (countryMembership.getUser().getUserId().equals(tradedArticle.getUserId())
					&& argentina.getArticleId().equals(tradedArticle.getArticleId())) {
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

}
