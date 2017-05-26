package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.JsonLinkSupport;

public class WantItemJson extends JsonLinkSupport {

	private Integer wantItemId;
	private Integer priority;
	private ItemJson item;

	public ItemJson getItem() {
		return item;
	}

	public Integer getPriority() {
		return priority;
	}

	public Integer getWantItemId() {
		return wantItemId;
	}

	public void setItem(ItemJson item) {
		this.item = item;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public void setWantItemId(Integer wantItemId) {
		this.wantItemId = wantItemId;
	}

}
