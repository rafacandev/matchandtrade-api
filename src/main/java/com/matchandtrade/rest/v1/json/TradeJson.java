package com.matchandtrade.rest.v1.json;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.matchandtrade.rest.JsonLinkSupport;
import com.matchandtrade.rest.v1.controller.TradeController;

public class TradeJson extends JsonLinkSupport {

	private String name;
	private Integer tradeId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonInclude(value=Include.NON_NULL)
	public Integer getTradeId() {
		return tradeId;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	@Override
	public void buildLinks() {
		if (getTradeId() != null) {
			add(linkTo(methodOn(TradeController.class).get(getTradeId())).withSelfRel());
		}
	}
}
