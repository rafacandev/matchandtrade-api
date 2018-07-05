package com.matchandtrade.rest.v1.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.matchandtrade.rest.JsonLinkSupport;

public class ItemJson extends JsonLinkSupport {

	private String description;
	private Integer articleId;
	private String name;

	public String getDescription() {
		return this.description;
	}

	@JsonInclude(value=Include.NON_NULL)
	public Integer getArticleId() {
		return articleId;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
