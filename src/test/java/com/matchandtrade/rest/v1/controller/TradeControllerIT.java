package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.TradeHelper;
import com.matchandtrade.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.matchandtrade.persistence.entity.TradeEntity.State.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class TradeControllerIT extends BaseControllerIT {

	@Autowired
	private TradeHelper tradeHelper;
	@Autowired
	private TradeRepositoryFacade tradeRepositoryFacade;
	private TradeTransformer tradeTransformer = new TradeTransformer();

	@Before
	public void before() {
		super.before();
	}

	@Test
	public void delete_When_TradeExists_Then_Succeeds() throws Exception {
		TradeEntity expected = tradeHelper.createPersistedEntity(authenticatedUser);
		mockMvc
			.perform(
				delete("/matchandtrade-api/v1/trades/{tradeId}", expected.getTradeId())
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtil.toJson(expected))
			)
			.andExpect(status().isNoContent());
	}

	@Test
	public void getAll_When_TradesExist_Then_Succeeds() throws Exception {
		tradeHelper.createPersistedEntity();
		MockHttpServletResponse response = mockMvc.perform(get("/matchandtrade-api/v1/trades/"))
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();

		List<TradeJson> trades = JsonUtil.fromArrayString(response.getContentAsString(), TradeJson.class);
		assertTrue(trades.size() > 1);
	}

	@Test
	public void get_When_GetByTradeId_Then_Succeeds() throws Exception {
		TradeEntity expected = tradeHelper.createPersistedEntity();
		MockHttpServletResponse response = mockMvc.perform(
				get("/matchandtrade-api/v1/trades/{tradeId}", expected.getTradeId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();

		TradeJson actual = JsonUtil.fromString(response.getContentAsString(), TradeJson.class);
		assertEquals(tradeTransformer.transform(expected), actual);
	}

	@Test
	public void post_When_TradeWithUniqueNames_Then_Succeeds() throws Exception {
		TradeJson expected = TradeHelper.createRandomJson();
		MockHttpServletResponse response = mockMvc
			.perform(
				post("/matchandtrade-api/v1/trades/")
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(expected))
			)
			.andExpect(status().isCreated())
			.andReturn()
			.getResponse();

		TradeJson actual = JsonUtil.fromString(response.getContentAsString(), TradeJson.class);
		expected.setTradeId(actual.getTradeId());
		assertEquals(expected, actual);
	}

	@Test
	public void put_When_TradeExists_Then_Succeeds() throws Exception {
		TradeEntity expected = tradeHelper.createPersistedEntity(authenticatedUser);
		MockHttpServletResponse response = mockMvc
			.perform(
				put("/matchandtrade-api/v1/trades/{tradeId}", expected.getTradeId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(expected))
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();

		TradeJson actual = JsonUtil.fromString(response.getContentAsString(), TradeJson.class);
		assertEquals(tradeTransformer.transform(expected), actual);
	}

	@Test
	public void put_When_TradeStateIsUpdatedToGenerateResults_Then_TriggersGeneratingResultsProcess() throws Exception {
		TradeEntity expected = tradeHelper.createPersistedEntity(authenticatedUser);
		expected.setState(GENERATE_RESULTS);
		MockHttpServletResponse response = mockMvc
			.perform(
				put("/matchandtrade-api/v1/trades/{tradeId}", expected.getTradeId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(expected))
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();

		TradeJson actual = JsonUtil.fromString(response.getContentAsString(), TradeJson.class);
		assertEquals(tradeTransformer.transform(expected), actual);

		TradeEntity tradeGeneratingResults = tradeRepositoryFacade.findByTradeId(actual.getTradeId());
		assertTrue(tradeGeneratingResults.getState() == GENERATING_RESULTS || tradeGeneratingResults.getState() == RESULTS_GENERATED);
	}
}
