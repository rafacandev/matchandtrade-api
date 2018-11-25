package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.ArticleHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class ArticleServiceIT {

	@Autowired
	private ArticleService fixture;
	@Autowired
	private ArticleHelper articleHelper;

	/**
	 * Alert, this test is not tread save.
	 * In a multi-threaded environment, when asserting results, assertion {@code assertTrue(actual.getResultList().contains(article))}
	 * might fail if other tests inserted many articles in the data storage; resulting in a false positive.
	 */
	 // TODO: Review this
	@Test
	public void get_When_ThereIsOneArticleForTheUser_Then_ReturnsOneArticle() {
		// Setting a long page size
		int pageSize = 50;
		int startingTotal = (int) fixture.findAll(1, pageSize).getPagination().getTotal();
		ArticleEntity article = articleHelper.createPersistedEntity();
		SearchResult<ArticleEntity> actual = fixture.findAll((startingTotal/pageSize)+1, pageSize);
		assertTrue(actual.getPagination().getTotal() > startingTotal); // This assertion should e enough for most cases
		assertTrue(actual.getResultList().contains(article));
	}

}
