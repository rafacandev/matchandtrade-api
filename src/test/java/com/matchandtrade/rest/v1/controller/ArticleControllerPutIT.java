package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleControllerPutIT {

	private ArticleController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private UserRandom userRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getArticleController(true);
		}
	}

	@Test
	public void shouldEditArticle() {
		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleEntity existingArticle = articleRandom.nextPersistedEntity(existingTradeMemberhip);
		ArticleJson request = ArticleTransformer.transform(existingArticle);
		
		String articleName = "ArticleName";
		String articleDescription = "ArticleDescription";
		request.setName(articleName);
		request.setDescription(articleDescription);
		
		ArticleJson response = fixture.put(existingTradeMemberhip.getTradeMembershipId(), request.getArticleId(), request);
		assertEquals(articleName, response.getName());
		assertEquals(articleDescription, response.getDescription());
	}	

	@Test(expected=RestException.class)
	public void shouldErrorWhenEditingAnInvalidArticleId() {
		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleJson request = ArticleRandom.nextJson();
		fixture.put(existingTradeMemberhip.getTradeMembershipId(), -1, request);
	}

	@Test(expected=RestException.class)
	public void shouldErrorWhenAnArticleWithTheSameNameAlreadyExists() {
		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleEntity existingArticle = articleRandom.nextPersistedEntity(existingTradeMemberhip);
		ArticleEntity existingArticle2 = articleRandom.nextPersistedEntity(existingTradeMemberhip);
		ArticleJson request = new ArticleJson();
		request.setName(existingArticle.getName());
		fixture.put(existingTradeMemberhip.getTradeMembershipId(), existingArticle2.getArticleId(), request);
	}
	
	@Test(expected=RestException.class)
	public void shouldErrorTryingToEditAnArticleThatNotBelongsToTheCurrentUser() {
		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		ArticleEntity existingArticle = articleRandom.nextPersistedEntity(existingTradeMemberhip);
		ArticleJson request = ArticleTransformer.transform(existingArticle);
		request.setName(request.getName() + "-Updated");
		fixture.put(existingTradeMemberhip.getTradeMembershipId(), request.getArticleId(), request);
	}
	
}
