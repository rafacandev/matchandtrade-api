package com.matchandtrade.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "trade_membership")
public class TradeMembershipEntity implements com.matchandtrade.persistence.entity.Entity {
	
	public enum Field {
		userId("user.userId"), tradeId("trade.tradeId"), tradeMembershipId("membershipId"), type("type");
		private String text;
		Field(String text) {
			this.text = text;
		}
		@Override
		public String toString() {
			return text;
		}
	}
	
	public enum Type {
		OWNER, MEMBER
	}
	
	private Integer tradeMembershipId;
	private TradeEntity trade;
	private UserEntity user;
	private Set<ItemEntity> items = new HashSet<>();
	
	private Type type;

	@OneToOne
	@JoinColumn(name="trade_id")
	public TradeEntity getTrade() {
		return trade;
	}

	@Id
	@Column(name = "trade_membership_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getTradeMembershipId() {
		return tradeMembershipId;
	}

	@Column(name="type")
	public Type getType() {
		return type;
	}

	@OneToOne
	@JoinColumn(name="user_id")
	public UserEntity getUser() {
		return user;
	}

	public void setTrade(TradeEntity trade) {
		this.trade = trade;
	}

	public void setTradeMembershipId(Integer tradeMembershipId) {
		this.tradeMembershipId = tradeMembershipId;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@OneToMany
	public Set<ItemEntity> getItems() {
		return items;
	}

	public void setItems(Set<ItemEntity> items) {
		this.items = items;
	}
	
}
