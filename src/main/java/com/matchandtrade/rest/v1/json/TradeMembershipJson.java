package com.matchandtrade.rest.v1.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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

	@JsonInclude(value=Include.NON_NULL)
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
