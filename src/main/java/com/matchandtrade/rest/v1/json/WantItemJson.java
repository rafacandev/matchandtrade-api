package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.JsonLinkSupport;

public class WantItemJson extends JsonLinkSupport {

	private Integer wantItemId;
	private Integer itemId;
	private Integer priority;

	public Integer getItemId() {
		return itemId;
	}

	public Integer getPriority() {
		return priority;
	}

	public Integer getWantItemId() {
		return wantItemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public void setWantItemId(Integer wantItemId) {
		this.wantItemId = wantItemId;
	}

}
