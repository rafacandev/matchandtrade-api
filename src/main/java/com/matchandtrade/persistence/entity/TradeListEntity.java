package com.matchandtrade.persistence.entity;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "trade_list")
public class TradeListEntity implements com.matchandtrade.persistence.entity.Entity {
	
	public enum Field {
		tradeListId, name, tradeItems, updatedDateTime
	}

	private Integer tradeListId;
	private String name;
	private Set<TradeItemEntity> tradeItems = new LinkedHashSet<TradeItemEntity>();
	private Date updatedDateTime;
	
	@Column(name = "name", length = 120, nullable = false, unique = false)
	public String getName() {
		return name;
	}

	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(
		name = "trade_list__trade_item",
		joinColumns = @JoinColumn(name = "trade_list_id"),
		inverseJoinColumns = @JoinColumn(name = "trade_item_id"))
	public Set<TradeItemEntity> getTradeItems() {
		return tradeItems;
	}

	@Id
	@Column(name = "trade_list_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getTradeListId() {
		return tradeListId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_date_time", nullable=false)
	public Date getUpdatedDateTime() {
		return this.updatedDateTime;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	
	public void setTradeItems(Set<TradeItemEntity> tradeItems) {
		this.tradeItems = tradeItems;
	}

	public void setTradeListId(Integer tradeListId) {
		this.tradeListId = tradeListId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tradeItems == null) ? 0 : tradeItems.hashCode());
		result = prime * result + ((updatedDateTime == null) ? 0 : updatedDateTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradeListEntity other = (TradeListEntity) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tradeItems == null) {
			if (other.tradeItems != null)
				return false;
		} else if (!tradeItems.equals(other.tradeItems))
			return false;
		if (updatedDateTime == null) {
			if (other.updatedDateTime != null)
				return false;
		} else if (!updatedDateTime.equals(other.updatedDateTime))
			return false;
		return true;
	}
	
	public TradeListEntity() {
		updatedDateTime = new Date();
	}

}
