package com.matchandtrade.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "trade_membership")
public class TradeMembershipEntity implements com.matchandtrade.persistence.entity.Entity {
	
	public enum Type {
		OWNER, MEMBER
	}
	
	private Integer tradeMembershipId;
	private Set<ItemEntity> items = new HashSet<>();
	private TradeEntity trade;
	private UserEntity user;
	private Type type;

	@OneToMany
	@JoinTable(name = "trade_membership_to_item", joinColumns = @JoinColumn(name = "trade_membership_id", foreignKey=@ForeignKey(name="trade_membership_to_item_trade_membership_id_fk")), inverseJoinColumns = @JoinColumn(name = "item_id", foreignKey=@ForeignKey(name="trade_membership_to_item_item_id_fk")))
	public Set<ItemEntity> getItems() {
		return items;
	}

	@OneToOne
	@JoinColumn(name="trade_id", foreignKey = @ForeignKey(name = "trade_membership_trade_id_fk"))
	public TradeEntity getTrade() {
		return trade;
	}

	@Id
	@Column(name = "trade_membership_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getTradeMembershipId() {
		return tradeMembershipId;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="type", nullable=false)
	public Type getType() {
		return type;
	}

	@OneToOne
	@JoinColumn(name="user_id", foreignKey = @ForeignKey(name = "trade_membership_user_id_fk"))
	public UserEntity getUser() {
		return user;
	}

	public void setItems(Set<ItemEntity> items) {
		this.items = items;
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

}
