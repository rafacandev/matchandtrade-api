package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.helper.ControllerHelper;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "file:config/matchandtrade.properties")
@SpringBootTest
@WebAppConfiguration
public class UserControllerIT {

	private String authorizationHeader;
	@Autowired
	private ControllerHelper controllerHelper;
	private MockMvc mockMvc;
	private UserEntity user;
	@Autowired
	private UserHelper userHelper;
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

	@Test
	public void get_When_UserExistsAndRequestInfoForTheSameUser_Then_ReturnAllUserData() throws Exception {
		MockHttpServletResponse response = mockMvc
			.perform(
				get("/matchandtrade-api/v1/users/{userId}", user.getUserId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();
		UserJson actual = JsonUtil.fromString(response.getContentAsString(), UserJson.class);
		assertEquals(user.getUserId(), actual.getUserId());
		assertEquals(user.getName(), actual.getName());
		assertEquals(user.getEmail(), actual.getEmail());
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
		String expectedName = user.getName() + " - updated";
		user.setName(expectedName);
		MockHttpServletResponse response = mockMvc
			.perform(
				put("/matchandtrade-api/v1/users/{userId}", user.getUserId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(user))
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();
		UserJson actual = JsonUtil.fromString(response.getContentAsString(), UserJson.class);
		assertEquals(expectedName, actual.getName());
	}

}
