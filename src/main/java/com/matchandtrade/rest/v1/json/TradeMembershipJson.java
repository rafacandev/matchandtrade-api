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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((tradeMembershipId == null) ? 0 : tradeMembershipId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradeMembershipJson other = (TradeMembershipJson) obj;
		if (tradeMembershipId == null) {
			if (other.tradeMembershipId != null)
				return false;
		} else if (!tradeMembershipId.equals(other.tradeMembershipId))
			return false;
		return true;
	}
}
