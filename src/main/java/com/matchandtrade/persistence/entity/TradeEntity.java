package com.matchandtrade.persistence.entity;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
@Table(name = "trade")
public class TradeEntity implements com.matchandtrade.persistence.entity.Entity {

	public enum State {
		SUBMITTING_ARTICLES,
		MATCHING_ARTICLES,
		ARTICLES_MATCHED,
		GENERATE_RESULTS,
		GENERATING_RESULTS,
		RESULTS_GENERATED,
		CANCELED
	}
	
	private String description;
	private Integer tradeId;
	private String name;
	private State state;
	private TradeResultEntity result;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TradeEntity that = (TradeEntity) o;
		return Objects.equals(description, that.description) &&
			Objects.equals(tradeId, that.tradeId) &&
			Objects.equals(name, that.name);
	}

	@Column(name = "description", length = 25000, nullable = false, unique = false)
	public String getDescription() {
		return description;
	}
	
	@Column(name = "name", length = 150, nullable = false, unique = true)
	public String getName() {
		return name;
	}

	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="trade_result_id")
	public TradeResultEntity getResult() {
		return result;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "state", length = 50, unique = false)
	public State getState() {
		return state;
	}

	@Id
	@Column(name = "trade_id")
	@SequenceGenerator(name="trade_id_generator", sequenceName = "trade_id_sequence")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "trade_id_generator")
	public Integer getTradeId() {
		return tradeId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, tradeId, name, state);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setResult(TradeResultEntity result) {
		this.result = result;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

}
