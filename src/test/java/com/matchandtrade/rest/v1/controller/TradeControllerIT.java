package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;
import com.matchandtrade.test.helper.ControllerHelper;
import com.matchandtrade.test.helper.TradeHelper;
import com.matchandtrade.test.helper.UserHelper;
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

import java.util.List;

import static com.matchandtrade.persistence.entity.TradeEntity.State.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "file:config/matchandtrade.properties")
@SpringBootTest
@WebAppConfiguration
public class TradeControllerIT {

	private String authorizationHeader;
	@Autowired
	private ControllerHelper controllerHelper;
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	@Autowired
	private TradeHelper tradeHelper;
	@Autowired
	private TradeRepositoryFacade tradeRepositoryFacade;
	private TradeTransformer tradeTransformer = new TradeTransformer();
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

	@Test
	public void delete_When_TradeExists_Then_Succeeds() throws Exception {
		TradeEntity expected = tradeHelper.createPersistedEntity(user);
		MockHttpServletResponse response = mockMvc
			.perform(
				delete("/matchandtrade-api/v1/trades/{tradeId}", expected.getTradeId())
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtil.toJson(expected))
			)
			.andExpect(status().isNoContent())
			.andReturn()
			.getResponse();
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
		TradeEntity expected = tradeHelper.createPersistedEntity(user);
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
		TradeEntity expected = tradeHelper.createPersistedEntity(user);
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

		TradeEntity tradeGeneratingResults = tradeRepositoryFacade.find(actual.getTradeId());
		assertTrue(tradeGeneratingResults.getState() == GENERATING_RESULTS || tradeGeneratingResults.getState() == RESULTS_GENERATED);
	}

}
