package com.matchandtrade.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "trade_result")
public class TradeResultEntity implements com.matchandtrade.persistence.entity.Entity {

	private Integer tradeResultId;
	private String text;

	@Id
	@Column(name = "trade_result_id", nullable = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY) // TODO: Change all from identity to sequence
	public Integer getTradeResultId() {
		return tradeResultId;
	}

	public void setTradeResultId(Integer tradeResultId) {
		this.tradeResultId = tradeResultId;
	}

	@Lob
	@Column(name = "text", nullable = false, unique = false)
	public String getText() {
		return text;
	}

	public void setText(String result) {
		this.text = result;
	}

}
