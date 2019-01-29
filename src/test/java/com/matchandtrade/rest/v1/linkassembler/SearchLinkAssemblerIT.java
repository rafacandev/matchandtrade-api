package com.matchandtrade.rest.v1.linkassembler;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.rest.Json;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.ArticleHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class SearchLinkAssemblerIT {
	@Autowired
	private ArticleHelper articleHelper;
	@Autowired
	private SearchLinkAssembler fixture;

	@Test
	public void when_RecipeIsArticles_Then_AssembleArticleLinks() {
		ArticleJson expectedArticle1 = articleHelper.createRandomJson();
		expectedArticle1.setArticleId(1);
		ArticleJson expectedArticle2 = articleHelper.createRandomJson();
		expectedArticle2.setArticleId(2);
		List<Json> resultList = new ArrayList<>();
		resultList.add(expectedArticle1);
		resultList.add(expectedArticle2);
		SearchResult<Json> searchResult = new SearchResult<>(resultList, new Pagination());
		fixture.assemble(searchResult);
		ArticleJson actual1 = (ArticleJson) searchResult.getResultList().get(0);
		ArticleJson actual2 = (ArticleJson) searchResult.getResultList().get(1);
		assertEquals(buildExpectedSelfUrl(expectedArticle1), actual1.getLinks().get(0).toString());
		assertEquals(buildExpectedSelfUrl(expectedArticle2), actual2.getLinks().get(0).toString());
	}

	private String buildExpectedSelfUrl(ArticleJson actual) {
		return "self=http://localhost/matchandtrade-api/v1/articles/" + actual.getArticleId();
	}
}
