package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.JsonLinkSupport;

public class ListingJson extends JsonLinkSupport {

	private Integer membershipId;
	private Integer articleId;

	/**
	 * Default empty contructor required for JSON serialization
	 */
	public ListingJson() { }

	public ListingJson(Integer membershipId, Integer articleId) {
		this.membershipId = membershipId;
		this.articleId = articleId;
	}

	public Integer getMembershipId() {
		return membershipId;
	}

	public void setMembershipId(Integer membershipId) {
		this.membershipId = membershipId;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

}
