package com.matchandtrade.rest.v1.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.matchandtrade.rest.JsonLinkSupport;

import java.util.Objects;

public class MembershipJson extends JsonLinkSupport {

	public enum Type {
		OWNER, MEMBER
	}
	
	private Integer membershipId;
	private Integer userId;
	private Integer tradeId;
	private Type type;


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MembershipJson that = (MembershipJson) o;
		return Objects.equals(membershipId, that.membershipId) &&
			Objects.equals(userId, that.userId) &&
			Objects.equals(tradeId, that.tradeId) &&
			type == that.type;
	}

	public Integer getTradeId() {
		return tradeId;
	}

	@JsonInclude(value=Include.NON_NULL)
	public Integer getMembershipId() {
		return membershipId;
	}

	@JsonInclude(value=Include.NON_NULL)
	public Type getType() {
		return type;
	}

	public Integer getUserId() {
		return userId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(membershipId, userId, tradeId, type);
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	public void setMembershipId(Integer membershipId) {
		this.membershipId = membershipId;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
