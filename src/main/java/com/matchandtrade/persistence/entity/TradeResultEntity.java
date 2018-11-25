package com.matchandtrade.persistence.entity;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "trade_result")
public class TradeResultEntity implements com.matchandtrade.persistence.entity.Entity {

	private Integer tradeResultId;
	private String tradeMaximizerOutput;
	private String csv;
	private String json;

	@Lob
	@Column(name = "csv", nullable = true, unique = false)
	public String getCsv() {
		return csv;
	}

	@Lob
	@Column(name = "trade_maximizer_output", nullable = false, unique = false)
	public String getTradeMaximizerOutput() {
		return tradeMaximizerOutput;
	}

	@Id
	@Column(name = "trade_result_id", nullable = false)
	@SequenceGenerator(name="trade_result_id_generator", sequenceName = "trade_result_id_sequence")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "trade_result_id_generator")
	public Integer getTradeResultId() {
		return tradeResultId;
	}

	public void setCsv(String csv) {
		this.csv = csv;
	}

	public void setTradeMaximizerOutput(String tradeMaximizerOutput) {
		this.tradeMaximizerOutput = tradeMaximizerOutput;
	}

	public void setTradeResultId(Integer tradeResultId) {
		this.tradeResultId = tradeResultId;
	}

	@Lob
	@Column(name = "json", nullable = true, unique = false)
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}
