package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.rest.v1.json.ListingJson;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.ArticleHelper;
import com.matchandtrade.test.helper.MembershipHelper;
import com.matchandtrade.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
@WebAppConfiguration
public class ListingControllerIT extends BaseControllerIT {

	@Autowired
	private ArticleHelper articleHelper;
	@Autowired
	private MembershipHelper membershipHelper;

	@Before
	public void before() {
		super.before();
	}

	private ListingJson buildListingWhenUserOwnsArticleAndMembership() {
		ArticleEntity article = articleHelper.createPersistedEntity(authenticatedUser);
		MembershipEntity membership = membershipHelper.createPersistedEntity(authenticatedUser);
		ListingJson expected = new ListingJson();
		expected.setArticleId(article.getArticleId());
		expected.setMembershipId(membership.getMembershipId());
		return expected;
	}

	@Test
	public void post_When_UserOwnsArticleAndMembership_Then_Succeeds() throws Exception {
		ListingJson expected = buildListingWhenUserOwnsArticleAndMembership();
		mockMvc
			.perform(
				post("/matchandtrade-api/v1/listing/")
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtil.toJson(expected))
			)
			.andExpect(status().isCreated());
	}

	@Test
	public void delete_When_UserOwnsArticleAndMembership_Then_Succeeds() throws Exception {
		ListingJson expected = buildListingWhenUserOwnsArticleAndMembership();
		mockMvc
			.perform(
				delete("/matchandtrade-api/v1/listing/")
					.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonUtil.toJson(expected))
			)
			.andExpect(status().isNoContent());
	}

}
