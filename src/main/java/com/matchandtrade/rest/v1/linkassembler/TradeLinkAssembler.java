package com.matchandtrade.rest.v1.linkassembler;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.rest.v1.json.TradeJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class TradeLinkAssembler {
	@Autowired
	private EntityLinks entityLinks;

	public void assemble(SearchResult<TradeJson> response) {
		for (TradeJson tradeJson : response.getResultList()) {
			Link link = entityLinks.linkToSingleResource(TradeJson.class, tradeJson.getTradeId());
			tradeJson.add(link);
		}
	}
}
