package com.matchandtrade.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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
	
	private Set<ItemEntity> items = new HashSet<>();
	private Set<OfferEntity> offers = new HashSet<>();
	private TradeEntity trade;
	private Integer tradeMembershipId;
	private Type type;
	private UserEntity user;

	@OneToMany
	@JoinTable(name="trade_membership_to_item", joinColumns=@JoinColumn(name="trade_membership_id", foreignKey=@ForeignKey(name="trade_membership_to_item_trade_membership_id_fk")), inverseJoinColumns = @JoinColumn(name="item_id", foreignKey=@ForeignKey(name="trade_membership_to_item_item_id_fk")))
	public Set<ItemEntity> getItems() {
		return items;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name="trade_membership_to_offer", joinColumns=@JoinColumn(name="trade_membership_id", foreignKey=@ForeignKey(name="trade_membership_to_offer_trade_membership_id_fk")), inverseJoinColumns=@JoinColumn(name="offer_id", foreignKey=@ForeignKey(name="trade_membership_to_offer_offer_id_fk")))
	public Set<OfferEntity> getOffers() {
		return offers;
	}

	@OneToOne
	@JoinColumn(name="trade_id", foreignKey=@ForeignKey(name="trade_membership_trade_id_fk"))
	public TradeEntity getTrade() {
		return trade;
	}

	@Id
	@Column(name="trade_membership_id")
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
	@JoinColumn(name="user_id", foreignKey=@ForeignKey(name="trade_membership_user_id_fk"))
	public UserEntity getUser() {
		return user;
	}

	public void setItems(Set<ItemEntity> items) {
		this.items = items;
	}

	public void setOffers(Set<OfferEntity> offers) {
		this.offers = offers;
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
