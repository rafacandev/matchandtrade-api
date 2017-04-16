package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.JsonLinkSupport;

public class TradeMembershipJson extends JsonLinkSupport {

	private Integer tradeMembershipId;
	private Integer userId;
	private Integer tradeId;

	@Override
	public void buildLinks() {
	}

	public Integer getTradeId() {
		return tradeId;
	}

	public Integer getTradeMembershipId() {
		return tradeMembershipId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	public void setTradeMembershipId(Integer tradeMembershipId) {
		this.tradeMembershipId = tradeMembershipId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
