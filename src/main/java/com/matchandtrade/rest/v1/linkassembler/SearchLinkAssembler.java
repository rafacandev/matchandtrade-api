package com.matchandtrade.rest.v1.linkassembler;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.rest.Json;
import com.matchandtrade.rest.v1.json.ArticleJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchLinkAssembler {

	@Autowired
	private ArticleLinkAssembler articleLinkAssembler;

	public void assemble(SearchResult<Json> searchResult) {
		searchResult.getResultList().forEach(json -> {
			ArticleJson article = (ArticleJson) json;
			articleLinkAssembler.assemble(article);
		});
	}
}
