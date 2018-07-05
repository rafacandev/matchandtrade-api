package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.JsonLinkSupport;

public class OfferJson extends JsonLinkSupport {

	private Integer offerId;
	private Integer offeredArticleId;
	private Integer wantedArticleId;

	public Integer getOfferedArticleId() {
		return offeredArticleId;
	}

	public Integer getOfferId() {
		return offerId;
	}

	public Integer getWantedArticleId() {
		return wantedArticleId;
	}

	public void setOfferedArticleId(Integer offeredArticleId) {
		this.offeredArticleId = offeredArticleId;
	}

	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
	}

	public void setWantedArticleId(Integer wantedArticleId) {
		this.wantedArticleId = wantedArticleId;
	}

}
