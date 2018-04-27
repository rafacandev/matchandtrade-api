package com.matchandtrade.rest.v1.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.matchandtrade.rest.JsonLinkSupport;

public class ItemJson extends JsonLinkSupport {

	private String description;
	private Integer itemId;
	private String name;

	public String getDescription() {
		return this.description;
	}

	@JsonInclude(value=Include.NON_NULL)
	public Integer getItemId() {
		return itemId;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
