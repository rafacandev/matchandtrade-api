package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.JsonLinkSupport;

public class TradeJson extends JsonLinkSupport {

	private String name;
	private Integer tradeId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTradeId() {
		return tradeId;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	@Override
	public void buildLinks() {
		// if (getTradeItemId() != null) {
		// add(linkTo(methodOn(TradeItemController.class).get(getTradeItemId())).withSelfRel());
		// }
	}
}
