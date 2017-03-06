//package com.matchandtrade.persistence.entity;
//
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//
//@Entity
//@Table(name="trade_item")
//public class TradeItemEntity implements com.matchandtrade.persistence.entity.Entity {
//	
//	public enum Field {
//		description, name, tradeItemId, updatedDateTime
//	}
//
//	private String description;
//	private String name;
//	private Integer tradeItemId;
//	private Date updatedDateTime;
//		
//	@Column(name="description", length=500, nullable=true, unique=false)
//	public String getDescription() {
//		return description;
//	}
//
//	@Column(name="name", length=120, nullable=false, unique=false)
//	public String getName() {
//		return name;
//	}
//
//	@Id
//	@Column(name="trade_item_id")
//	@GeneratedValue
//	public Integer getTradeItemId() {
//		return tradeItemId;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="updated_date_time", nullable=false)
//	public Date getUpdatedDateTime() {
//		return this.updatedDateTime;
//	}
//	
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public void setUpdatedDateTime(Date updatedDateTime) {
//		this.updatedDateTime = updatedDateTime;
//	}
//	
//	public void setTradeItemId(Integer tradeItemId) {
//		this.tradeItemId = tradeItemId;
//	}
//	
//	@Override
//	public boolean equals(Object other) {
//		if (other == null) {
//			return false;
//		} else if (!(other instanceof TradeListEntity)) {
//			return false;
//		}
//		TradeItemEntity otherCasted = (TradeItemEntity) other;
//		if (tradeItemId != null && otherCasted.getTradeItemId() != null) {
//			return  false;
//		} else if (!tradeItemId.equals(otherCasted.getTradeItemId())){
//			return false;
//		}
//		return true;
//	}
//	
//	@Override
//	public int hashCode() {
//		int salt = 1;
//		if (tradeItemId != null) {
//			salt = tradeItemId;
//		}
//		return super.hashCode() * salt;
//	}
//	
//	public TradeItemEntity() {
//		updatedDateTime = new Date();
//	}
//	
//}
