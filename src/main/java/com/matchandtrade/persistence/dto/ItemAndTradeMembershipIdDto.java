package com.matchandtrade.persistence.dto;

import com.matchandtrade.persistence.entity.ItemEntity;

public class ItemAndTradeMembershipIdDto implements Dto {

	private ItemEntity item;
	private Integer tradeMembershipId;

	public ItemAndTradeMembershipIdDto(ItemEntity item, Integer tradeMembershipId) {
		this.tradeMembershipId = tradeMembershipId;
		this.item = item;
	}
	
	public ItemEntity getItem() {
		return item;
	}

	public Integer getTradeMembershipId() {
		return tradeMembershipId;
	}

}