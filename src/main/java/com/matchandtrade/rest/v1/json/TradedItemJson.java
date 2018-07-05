package com.matchandtrade.rest.v1.json;

public class TradedItemJson {
	private Integer userId;
	private String userName;
	private Integer articleId;
	private String itemName;
	private Integer receivingUserId;
	private String receivingUserName;
	private Integer receivingArticleId;
	private String receivingItemName;
	private Integer sendingUserId;
	private String sendingUserName;

	public Integer getArticleId() {
		return articleId;
	}

	public String getItemName() {
		return itemName;
	}

	public Integer getReceivingArticleId() {
		return receivingArticleId;
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

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setReceivingArticleId(Integer receivingArticleId) {
		this.receivingArticleId = receivingArticleId;
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
