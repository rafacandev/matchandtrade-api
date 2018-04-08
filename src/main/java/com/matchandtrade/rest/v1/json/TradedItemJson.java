package com.matchandtrade.rest.v1.json;

public class TradedItemJson {
	private Integer userId;
	private String userName;
	private Integer itemId;
	private String itemName;
	private Integer receivingUserId;
	private String receivingUserName;
	private Integer receivingItemId;
	private String receivingItemName;
	private Integer sendingUserId;
	private String sendingUserName;

	public Integer getItemId() {
		return itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public Integer getReceivingItemId() {
		return receivingItemId;
	}

	public String getReceivingItemName() {
		return receivingItemName;
	}

	public Integer getReceivingUserId() {
		return receivingUserId;
	}

	public String getReceivingUserName() {
		return receivingUserName;
	}

	public Integer getSendingUserId() {
		return sendingUserId;
	}

	public String getSendingUserName() {
		return sendingUserName;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setReceivingItemId(Integer receivingItemId) {
		this.receivingItemId = receivingItemId;
	}

	public void setReceivingItemName(String receivingItemName) {
		this.receivingItemName = receivingItemName;
	}

	public void setReceivingUserId(Integer receivingUserId) {
		this.receivingUserId = receivingUserId;
	}

	public void setReceivingUserName(String receivingUserName) {
		this.receivingUserName = receivingUserName;
	}

	public void setSendingUserId(Integer sendingUserId) {
		this.sendingUserId = sendingUserId;
	}

	public void setSendingUserName(String sendingUserName) {
		this.sendingUserName = sendingUserName;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
