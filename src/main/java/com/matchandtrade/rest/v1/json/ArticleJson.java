package com.matchandtrade.rest.v1.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.matchandtrade.rest.JsonLinkSupport;

public class ArticleJson extends JsonLinkSupport {

	private Integer articleId;
	private String name;

	@JsonInclude(value=Include.NON_NULL)
	public Integer getArticleId() {
		return articleId;
	}

	public String getName() {
		return name;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
