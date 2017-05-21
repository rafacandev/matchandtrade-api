package com.matchandtrade.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "trade")
public class TradeEntity implements com.matchandtrade.persistence.entity.Entity {

	public enum State {
		SUBMITTING_ITEMS, MATCHING_ITEMS, GENERATING_TRADES, CLOSED 
	}
	
	private Integer tradeId;
	private String name;
	private State state;

	@Column(name = "name", length = 150, nullable = false, unique = true)
	public String getName() {
		return name;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "state", length = 50, unique = false)
	public State getState() {
		return state;
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

	public void setState(State state) {
		this.state = state;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}
	
}
