package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.JsonLinkSupport;

public class OfferJson extends JsonLinkSupport {

	private Integer offerId;
	private Integer offeredItemId;
	private Integer wantedItemId;

	public Integer getOfferedItemId() {
		return offeredItemId;
	}

	public Integer getOfferId() {
		return offerId;
	}

	public Integer getWantedItemId() {
		return wantedItemId;
	}

	public void setOfferedItemId(Integer offeredItemId) {
		this.offeredItemId = offeredItemId;
	}

	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
	}

	public void setWantedItemId(Integer wantedItemId) {
		this.wantedItemId = wantedItemId;
	}

}
