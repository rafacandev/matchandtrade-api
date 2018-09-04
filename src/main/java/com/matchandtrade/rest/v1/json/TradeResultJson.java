package com.matchandtrade.rest.v1.json;

import java.util.ArrayList;
import java.util.List;

public class TradeResultJson {

	private Integer tradeId;
	private String tradeName;
	private Integer totalOfArticles;
	private Integer totalOfTradedArticles;
	private Integer totalOfNotTradedArticles;
	private List<TradedArticleJson> tradedArticles = new ArrayList<>();

	public Integer getTradeId() {
		return tradeId;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public Integer getTotalOfArticles() {
		return totalOfArticles;
	}

	public void setTotalOfArticles(Integer totalOfArticles) {
		this.totalOfArticles = totalOfArticles;
	}

	public Integer getTotalOfTradedArticles() {
		return totalOfTradedArticles;
	}

	public void setTotalOfTradedArticles(Integer totalOfTradedArticles) {
		this.totalOfTradedArticles = totalOfTradedArticles;
	}

	public Integer getTotalOfNotTradedArticles() {
		return totalOfNotTradedArticles;
	}

	public void setTotalOfNotTradedArticles(Integer totalOfNotTradedArticles) {
		this.totalOfNotTradedArticles = totalOfNotTradedArticles;
	}

	public List<TradedArticleJson> getTradedArticles() {
		return tradedArticles;
	}

	public void setTradedArticles(List<TradedArticleJson> tradedArticles) {
		this.tradedArticles = tradedArticles;
	}

}
