package com.matchandtrade.rest.v1.json;

import java.util.ArrayList;
import java.util.List;

public class TradeResultJson {

	private Integer tradeId;
	private String tradeName;
	private Integer totalOfItems;
	private Integer totalOfTradedItems;
	private Integer totalOfNotTradedItems;
	private List<TradedItemJson> tradedItems = new ArrayList<>();

	public Integer getTotalOfItems() {
		return totalOfItems;
	}

	public Integer getTotalOfNotTradedItems() {
		return totalOfNotTradedItems;
	}

	public Integer getTotalOfTradedItems() {
		return totalOfTradedItems;
	}

	public List<TradedItemJson> getTradedItems() {
		return tradedItems;
	}

	public Integer getTradeId() {
		return tradeId;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTotalOfItems(Integer totalOfItems) {
		this.totalOfItems = totalOfItems;
	}

	public void setTotalOfNotTradedItems(Integer totalOfNotTradedItems) {
		this.totalOfNotTradedItems = totalOfNotTradedItems;
	}

	public void setTotalOfTradedItems(Integer totalOfTradedItems) {
		this.totalOfTradedItems = totalOfTradedItems;
	}

	public void setTradedItems(List<TradedItemJson> tradedItems) {
		this.tradedItems = tradedItems;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

}
