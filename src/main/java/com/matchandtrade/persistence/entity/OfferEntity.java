package com.matchandtrade.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="offer")
public class OfferEntity implements com.matchandtrade.persistence.entity.Entity {

	private Integer offerId;
	private ItemEntity offeredItem;
	private ItemEntity wantedItem;
	
	@OneToOne
	@JoinColumn(name="offered_item_id")
	public ItemEntity getOfferedItem() {
		return offeredItem;
	}

	@Id
	@Column(name="offer_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getOfferId() {
		return offerId;
	}

	@OneToOne
	@JoinColumn(name="wanted_item_id")
	public ItemEntity getWantedItem() {
		return wantedItem;
	}

	public void setOfferedItem(ItemEntity offeredItem) {
		this.offeredItem = offeredItem;
	}

	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
	}

	public void setWantedItem(ItemEntity wantedItem) {
		this.wantedItem = wantedItem;
	}

	@Override
	public String toString() {
		return "OfferEntity [offerId=" + offerId + ", offeredItem=" + offeredItem + ", wantedItem=" + wantedItem + "]";
	}

}
