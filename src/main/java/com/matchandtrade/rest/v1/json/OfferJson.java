package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.JsonLinkSupport;

import java.util.Objects;

public class OfferJson extends JsonLinkSupport {

	private Integer offerId;
	private Integer offeredArticleId;
	private Integer wantedArticleId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OfferJson offerJson = (OfferJson) o;
		return Objects.equals(offerId, offerJson.offerId) &&
			Objects.equals(offeredArticleId, offerJson.offeredArticleId) &&
			Objects.equals(wantedArticleId, offerJson.wantedArticleId);
	}

	public Integer getOfferedArticleId() {
		return offeredArticleId;
	}

	public Integer getOfferId() {
		return offerId;
	}

	public Integer getWantedArticleId() {
		return wantedArticleId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(offerId, offeredArticleId, wantedArticleId);
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
