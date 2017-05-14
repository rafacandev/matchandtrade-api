package com.matchandtrade.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "trade")
public class TradeEntity implements com.matchandtrade.persistence.entity.Entity {
	
	public enum Field {
		tradeId, name
	}
	
	private Integer tradeId;
	private String name;

	@Column(name = "name", length = 150, nullable = false, unique = true)
	public String getName() {
		return name;
	}

	@Id
	@Column(name = "trade_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getTradeId() {
		return tradeId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}
	
}
