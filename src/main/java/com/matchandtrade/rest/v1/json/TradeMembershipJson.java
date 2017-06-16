package com.matchandtrade.rest.v1.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.matchandtrade.rest.JsonLinkSupport;

public class TradeMembershipJson extends JsonLinkSupport {

	public enum Type {
		OWNER, MEMBER
	}
	
	private Integer tradeMembershipId;
	private Integer userId;
	private Integer tradeId;
	private Type type;

	public Integer getTradeId() {
		return tradeId;
	}

	@JsonInclude(value=Include.NON_NULL)
	public Integer getTradeMembershipId() {
		return tradeMembershipId;
	}

	@JsonInclude(value=Include.NON_NULL)
	public Type getType() {
		return type;
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

	public void setType(Type type) {
		this.type = type;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
