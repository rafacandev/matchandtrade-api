package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.JsonLinkSupport;

public class ArticleJson extends JsonLinkSupport {

	private Integer articleId;
	private String description;
	private String name;

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
