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

	@Test
	public void get_When_ArticlesExist_Then_ReturnArticles() {
		int startingTotal = (int) fixture.findAll(1, 1).getPagination().getTotal();
		articleHelper.createPersistedEntity();
		SearchResult<ArticleEntity> actual = fixture.findAll(1, 1);
		assertTrue(actual.getPagination().getTotal() > startingTotal);
		assertEquals(1, actual.getResultList().size());
	}
}
