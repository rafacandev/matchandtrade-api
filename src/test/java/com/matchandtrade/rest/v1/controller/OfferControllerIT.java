package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.*;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import com.matchandtrade.persistence.facade.OfferRepositoryFacade;
import com.matchandtrade.rest.v1.json.OfferJson;
import com.matchandtrade.rest.v1.transformer.MembershipTransformer;
import com.matchandtrade.rest.v1.transformer.OfferTransformer;
import com.matchandtrade.test.helper.*;
import com.matchandtrade.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "file:config/matchandtrade.properties")
@SpringBootTest
@WebAppConfiguration
public class OfferControllerIT {

	@Autowired
	private ArticleHelper articleHelper;
	private String authorizationHeader;
	@Autowired
	private ControllerHelper controllerHelper;
	private MockMvc mockMvc;
	@Autowired
	private MembershipHelper membershipHelper;
	@Autowired
	private MembershipRepositoryFacade membershipRepositoryFacade;
	@Autowired
	private OfferRepositoryFacade offerRepositoryFacade;
	@Autowired
	private OfferTransformer offerTransformer;
	@Autowired
	private SearchHelper searchHelper;
	private UserEntity user;
	@Autowired
	private UserHelper userHelper;
	@Autowired
	private TradeHelper tradeHelper;
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		// Reusing user and authorization header for better performance
		if (user == null) {
			user = userHelper.createPersistedEntity();
			authorizationHeader = controllerHelper.generateAuthorizationHeader(user);
		}
	}

	private class OfferBuilder {
		ArticleEntity offeredArticle;
		ArticleEntity wantedArticle;
		MembershipEntity membership;
		OfferJson offer;
		public OfferBuilder build() {
			// Create a trade for a random user
			TradeEntity trade = tradeHelper.createPersistedEntity(user);

			// Create owner's articles (Greek letters)
			membership = searchHelper.findMembership(user, trade);
			offeredArticle = articleHelper.createPersistedEntity(membership);

			// Create member's articles (country names)
			UserEntity memberUser = userHelper.createPersistedEntity();
			MembershipEntity memberTradeMembership = membershipHelper.subscribeUserToTrade(memberUser, trade);
			wantedArticle = articleHelper.createPersistedEntity(memberTradeMembership);

			// Owner offers Alpha for Australia
			offer = new OfferJson();
			offer.setOfferedArticleId(offeredArticle.getArticleId());
			offer.setWantedArticleId(wantedArticle.getArticleId());
			return this;
		}

		public OfferEntity persistOffer() {
			OfferEntity offerEntity = new OfferEntity();
			offerEntity.setOfferedArticle(offeredArticle);
			offerEntity.setWantedArticle(wantedArticle);
			offerRepositoryFacade.save(offerEntity);
			membership.getOffers().add(offerEntity);
			membershipRepositoryFacade.save(membership);
			return offerEntity;
		}

	}

	@Test
	public void delete_When_UserOwnsOffer_Then_Succeeds() throws Exception {
		OfferBuilder offerBuilder = new OfferBuilder().build();
		Integer membershipId = offerBuilder.membership.getMembershipId();
		OfferEntity expectedEntity = offerBuilder.persistOffer();
		OfferJson expected = offerTransformer.transform(expectedEntity);
		mockMvc
			.perform(
				delete("/matchandtrade-api/v1/memberships/{membershipId}/offers/{offerId}", membershipId, expected.getOfferId())
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isNoContent());
	}

	@Test
	public void get_When_UserOwnsOffer_Then_Succeeds() throws Exception {
		OfferBuilder offerBuilder = new OfferBuilder().build();
		Integer membershipId = offerBuilder.membership.getMembershipId();
		OfferEntity expectedEntity = offerBuilder.persistOffer();
		OfferJson expected = offerTransformer.transform(expectedEntity);
		String response = mockMvc
			.perform(
				get("/matchandtrade-api/v1/memberships/{membershipId}/offers/{offerId}", membershipId, expected.getOfferId())
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtil.toJson(expected))
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();

		OfferJson actual = JsonUtil.fromString(response, OfferJson.class);
		assertEquals(expected, actual);
	}

	@Test
	public void get_When_GetByOfferedArticleIdAndOfferedArticleExists_Then_Succeeds() throws Exception {
		OfferBuilder offerBuilder = new OfferBuilder().build();
		Integer membershipId = offerBuilder.membership.getMembershipId();
		OfferEntity expectedEntity = offerBuilder.persistOffer();
		OfferJson expected = offerTransformer.transform(expectedEntity);
		String response = mockMvc
			.perform(
				get("/matchandtrade-api/v1/memberships/{membershipId}/offers?offeredArticleId={offeredArticleId}", membershipId, expected.getOfferedArticleId())
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();

		List<OfferJson> actualList = JsonUtil.fromArrayString(response, OfferJson.class);
		OfferJson actual = actualList.get(0);
		assertEquals(expected, actual);
	}

	@Test
	public void post_When_UserOwnsArticleAndMembership_Then_Succeeds() throws Exception {
		OfferBuilder offerBuilder = new OfferBuilder().build();
		OfferJson expected = offerBuilder.offer;
		String response = mockMvc
			.perform(
				post("/matchandtrade-api/v1/memberships/{membershipId}/offers/", offerBuilder.membership.getMembershipId())
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtil.toJson(expected))
			)
			.andExpect(status().isCreated())
			.andReturn()
			.getResponse()
			.getContentAsString();
		OfferJson actual = JsonUtil.fromString(response, OfferJson.class);
		expected.setOfferId(actual.getOfferId());
		assertEquals(expected, actual);
	}

}
