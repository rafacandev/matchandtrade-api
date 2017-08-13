package com.matchandtrade.rest.v1.json;

import org.springframework.hateoas.Link;

import com.matchandtrade.rest.JsonLinkSupport;

public class TradeResultJson extends JsonLinkSupport {

	private Link offeringItem;
	private Link receivingItem;

	/**
	 * Public default empty constructor
	 */
	public TradeResultJson() { }

	/**
	 * Basic constructor which sets the arguments as its properties.
	 * @param offeringItem
	 * @param receivingItem
	 */
	public TradeResultJson(Link offeringItem, Link receivingItem) {
		this.offeringItem = offeringItem;
		this.receivingItem = receivingItem;
	}
	
	
	public Link getOfferingItem() {
		return offeringItem;
	}

	public void setOfferingItem(Link offeringItem) {
		this.offeringItem = offeringItem;
	}

	public Link getReceivingItem() {
		return receivingItem;
	}

	public void setReceivingItem(Link receivingItem) {
		this.receivingItem = receivingItem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((offeringItem == null) ? 0 : offeringItem.hashCode());
		result = prime * result + ((receivingItem == null) ? 0 : receivingItem.hashCode());
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
		TradeResultJson other = (TradeResultJson) obj;
		if (offeringItem == null) {
			if (other.offeringItem != null)
				return false;
		} else if (!offeringItem.equals(other.offeringItem))
			return false;
		if (receivingItem == null) {
			if (other.receivingItem != null)
				return false;
		} else if (!receivingItem.equals(other.receivingItem))
			return false;
		return true;
	}

}
