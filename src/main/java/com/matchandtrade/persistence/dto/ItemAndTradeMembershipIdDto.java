package com.matchandtrade.persistence.dto;

import com.matchandtrade.persistence.entity.ArticleEntity;

public class ItemAndTradeMembershipIdDto implements Dto {

	private ArticleEntity item;
	private Integer tradeMembershipId;

	public ItemAndTradeMembershipIdDto(ArticleEntity item, Integer tradeMembershipId) {
		this.tradeMembershipId = tradeMembershipId;
		this.item = item;
	}
	
	public ArticleEntity getItem() {
		return item;
	}

	public Integer getTradeMembershipId() {
		return tradeMembershipId;
	}

}