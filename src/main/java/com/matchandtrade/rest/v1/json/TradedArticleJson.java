package com.matchandtrade.rest.v1.json;

public class TradedArticleJson {
	
	private Integer userId;
	private String userName;
	private Integer articleId;
	private String articleName;
	private Integer receivingUserId;
	private String receivingUserName;
	private Integer receivingArticleId;
	private String receivingArticleName;
	private Integer sendingUserId;
	private String sendingUserName;

	public Integer getArticleId() {
		return articleId;
	}

	public String getArticleName() {
		return articleName;
	}

	public Integer getReceivingArticleId() {
		return receivingArticleId;
	}

	public String getReceivingArticleName() {
		return receivingArticleName;
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

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public void setReceivingArticleId(Integer receivingArticleId) {
		this.receivingArticleId = receivingArticleId;
	}

	public void setReceivingArticleName(String receivingArticleName) {
		this.receivingArticleName = receivingArticleName;
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
