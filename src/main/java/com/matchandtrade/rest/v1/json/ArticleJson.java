package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.JsonLinkSupport;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ArticleJson that = (ArticleJson) o;
		return Objects.equals(articleId, that.articleId) &&
			Objects.equals(description, that.description) &&
			Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(articleId, description, name);
	}
}
