package com.matchandtrade.rest.v1.linkassembler;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.test.DefaultTestingConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class ArticleLinkAssemblerIT {

	@Autowired
	private ArticleLinkAssembler fixture;

	@Test
	public void asseble_When_JsonHasId_Then_AssembleSelfLink() {
		ArticleJson json = new ArticleJson();
		json.setArticleId(1);
		fixture.assemble(json);
		assertEquals("self", json.getLinks().get(0).getKey());
		assertEquals("http://localhost/matchandtrade-api/v1/articles/1", json.getLinks().get(0).getValue());
	}

	@Test
	public void assemble_When_SearchResultContainsJsonWithId_Then_AssembleSelfLinks() {
		ArticleJson json = new ArticleJson();
		json.setArticleId(1);
		SearchResult<ArticleJson> searchResult = new SearchResult<>(singletonList(json), null);
		fixture.assemble(searchResult);
		assertEquals("self", searchResult.getResultList().get(0).getLinks().get(0).getKey());
		assertEquals("http://localhost/matchandtrade-api/v1/articles/1", searchResult.getResultList().get(0).getLinks().get(0).getValue());
	}
}
