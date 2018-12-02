package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeEntity.State;
import com.matchandtrade.persistence.entity.TradeResultEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.TradeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.matchandtrade.persistence.entity.TradeEntity.State.RESULTS_GENERATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TradeResultValidatorUT {

	@Mock
	private TradeService mockTradeService;
	private TradeEntity existingTrade;
	private TradeResultValidator fixture;

	@Before
	public void before() {
		fixture = new TradeResultValidator();
		existingTrade = new TradeEntity();
		existingTrade.setTradeId(1);
		existingTrade.setState(RESULTS_GENERATED);
		existingTrade.setResult(new TradeResultEntity());

		when(mockTradeService.findByTradeId(existingTrade.getTradeId())).thenReturn(existingTrade);
		fixture.tradeService = mockTradeService;
	}

	@Test
	public void validateGet_When_TradeStateIsNotResultsGenerated_Then_BadRequest() {
		List<State> statesExpectedToFail = new LinkedList<>(Arrays.asList(State.values()));
		statesExpectedToFail.remove(RESULTS_GENERATED);
		for (State state : statesExpectedToFail) {
			existingTrade.setState(state);
			try {
				fixture.validateGet(existingTrade.getTradeId());
			} catch (RestException e) {
				assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
				assertEquals("TradeResult is only available when Trade.State is RESULTS_GENERATED", e.getDescription());
				continue;
			}
			fail("There was no exception for State: " + state);
		}
	}

	@Test(expected = RestException.class)
	public void validateGet_When_TradeHasNoResults_Then_InternalSeverErro() {
		existingTrade.setResult(null);
		try {
			fixture.validateGet(existingTrade.getTradeId());
		} catch (RestException e) {
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getHttpStatus());
			assertEquals("There is no results for Trade.tradeId: 1", e.getDescription());
			throw e;
		}
	}

	@Test
	public void validateGet_When_TradeHasResults_Then_Succeeds() {
		fixture.validateGet(existingTrade.getTradeId());
	}

}