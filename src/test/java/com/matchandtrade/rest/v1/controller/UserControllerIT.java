package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class UserControllerIT extends BaseControllerIT {

	@Before
	public void before() {
		super.before();
	}

	@Test
	public void get_When_UserExistsAndRequestInfoForTheSameUser_Then_ReturnAllUserData() throws Exception {
		MockHttpServletResponse response = mockMvc
			.perform(
				get("/matchandtrade-api/v1/users/{userId}", authenticatedUser.getUserId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();
		UserJson actual = JsonUtil.fromString(response.getContentAsString(), UserJson.class);
		assertEquals(authenticatedUser.getUserId(), actual.getUserId());
		assertEquals(authenticatedUser.getName(), actual.getName());
		assertEquals(authenticatedUser.getEmail(), actual.getEmail());
	}

	@Test
	public void get_When_UserExistsAndRequestInfoForDifferentUser_Then_ReturnOnlyIdAndName() throws Exception {
		UserEntity expected = userHelper.createPersistedEntity();
		MockHttpServletResponse response = mockMvc
			.perform(
				get("/matchandtrade-api/v1/users/{userId}", expected.getUserId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();
		UserJson actual = JsonUtil.fromString(response.getContentAsString(), UserJson.class);
		assertEquals(expected.getUserId(), actual.getUserId());
		assertEquals(expected.getName(), actual.getName());
		assertNull(actual.getEmail());
	}

	@Test
	public void put_When_UserExistsAndChangesItsName_Then_Succeeds() throws Exception {
		String expectedName = authenticatedUser.getName() + " - updated";
		authenticatedUser.setName(expectedName);
		MockHttpServletResponse response = mockMvc
			.perform(
				put("/matchandtrade-api/v1/users/{userId}", authenticatedUser.getUserId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(authenticatedUser))
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();
		UserJson actual = JsonUtil.fromString(response.getContentAsString(), UserJson.class);
		assertEquals(expectedName, actual.getName());
	}
}
