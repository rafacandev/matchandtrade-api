package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.JsonLinkSupport;

public class ListingJson extends JsonLinkSupport {

	private Integer mebershipId;
	private Integer articleId;

	public ListingJson(Integer membershipId, Integer articleId) {
		this.mebershipId = membershipId;
		this.articleId = articleId;
	}

	public Integer getMebershipId() {
		return mebershipId;
	}

	public void setMebershipId(Integer mebershipId) {
		this.mebershipId = mebershipId;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

}
