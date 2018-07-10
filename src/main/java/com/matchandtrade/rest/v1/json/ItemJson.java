package com.matchandtrade.rest.v1.json;

public class ItemJson extends ArticleJson {

	private String description;
	
	public ItemJson() {
		this.setType(ArticleJson.Type.ITEM);
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
