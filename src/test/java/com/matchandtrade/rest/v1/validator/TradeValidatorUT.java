package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.SearchService;
import com.matchandtrade.rest.service.TradeService;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.test.helper.StringRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class TradeValidatorUT {

	private TradeValidator fixture = new TradeValidator();
	@Mock
	private SearchService<MembershipEntity> searchServiceMembershipMock;
	@Mock
	private SearchService<TradeEntity> searchServiceTradeMock;
	@Mock
	private TradeService tradeService;

	@Before
	public void before() {
		SearchResult<TradeEntity> searchResultTrade = new SearchResult<>(new ArrayList<>(), new Pagination());
		doReturn(searchResultTrade).when(searchServiceTradeMock).search(any(), any());
		fixture.searchServiceTrade = searchServiceTradeMock;

		SearchResult<MembershipEntity> searchResultMembership = new SearchResult<>(new ArrayList<>(), new Pagination());
		doReturn(searchResultMembership).when(searchServiceMembershipMock).search(any(), any());
		fixture.searchServiceMembership = searchServiceMembershipMock;

		doReturn(new TradeEntity()).when(tradeService).find(1);
		fixture.tradeService = tradeService;
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_TradeDoesNotExist_Then_NotFound() {
		try {
			fixture.validateDelete(new UserEntity(), 0);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_UserDoesNotOwnTrade_Then_Succeeds() {
		try {
			fixture.validateDelete(new UserEntity(), 1);
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateGet_When_TradeDoesNotExist_Then_NotFound() {
		try {
			fixture.validateGet(0);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameAlreadyExists_Then_BadRequest() {
		List<TradeEntity> tradesWithDuplicatedName = Arrays.asList(new TradeEntity());
		SearchResult<TradeEntity> searchResultTrade = new SearchResult<>(tradesWithDuplicatedName, new Pagination());
		doReturn(searchResultTrade).when(searchServiceTradeMock).search(any(), any());

		TradeJson json = new TradeJson();
		json.setName("duplicated-name");
		try {
			fixture.validatePost(json);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_Description1001Characters_Then_BadRequest() {
		TradeJson json = new TradeJson();
		json.setName("name");
		json.setDescription(StringRandom.sequentialNumericString(1001));
		try {
			fixture.validatePost(json);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_DescriptionHas2Characters_Then_BadRequest() {
		TradeJson json = new TradeJson();
		json.setName("name");
		json.setDescription("ab");
		try {
			fixture.validatePost(json);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void validatePost_When_DescriptionIsNull_Then_Succeeds() {
		TradeJson json = new TradeJson();
		json.setName("name");
		fixture.validatePost(json);
	}

	@Test
	public void validatePost_When_DescriptionHas3Characters_Then_Succeeds() {
		TradeJson json = new TradeJson();
		json.setName("name");
		fixture.validatePost(json);
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameHas150Characters_Then_BadRequest() {
		TradeJson json = new TradeJson();
		json.setName(StringRandom.sequentialNumericString(151));
		try {
			fixture.validatePost(json);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameIsNull_Then_BadRequest() {
		TradeJson json = new TradeJson();
		try {
			fixture.validatePost(json);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameHas2Characters_Then_BadRequest() {
		TradeJson json = new TradeJson();
		json.setName("ab");
		try {
			fixture.validatePost(json);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void validatePost_When_NameHas3Characters_Then_Succeeds() {
		TradeJson json = new TradeJson();
		json.setName("abc");
		fixture.validatePost(json);
	}

	@Test(expected = RestException.class)
	public void validatePut_When_UserDoesNotOwnTrade_Then_Forbidden() {
		TradeJson json = new TradeJson();
		json.setTradeId(1);
		json.setName("name");
		try {
			fixture.validatePut(json, new UserEntity());
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePut_When_NameAlreadyExists_Then_BadRequest() {
		List<TradeEntity> tradesWithDuplicatedName = Arrays.asList(new TradeEntity());
		SearchResult<TradeEntity> searchResultTrade = new SearchResult<>(tradesWithDuplicatedName, new Pagination());
		doReturn(searchResultTrade).when(searchServiceTradeMock).search(any(), any());

		List<MembershipEntity> membershipsBelongingToUserForGivenTrade = Arrays.asList(new MembershipEntity());
		SearchResult<MembershipEntity> searchResultMemberships = new SearchResult<>(membershipsBelongingToUserForGivenTrade, new Pagination());
		doReturn(searchResultMemberships).when(searchServiceMembershipMock).search(any(), any());

		TradeJson json = new TradeJson();
		json.setTradeId(1);
		json.setName("duplicated-name");
		try {
			fixture.validatePut(json, new UserEntity());
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

}
