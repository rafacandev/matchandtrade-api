package com.matchandtrade.rest.v1.linkassembler;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.rest.v1.controller.TradeController;
import com.matchandtrade.rest.v1.json.TradeJson;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class TradeLinkAssembler {
	public void assemble(SearchResult<TradeJson> json) {
		for (TradeJson tradeJson : json.getResultList()) {
			assemble(tradeJson);
		}
	}

	public void assemble(TradeJson json) {
		Link self = linkTo(TradeController.class).slash(json.getTradeId()).withSelfRel();
		json.add("self", self.getHref());
	}
}
