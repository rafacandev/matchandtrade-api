package com.matchandtrade.persistence.dto;

import com.matchandtrade.persistence.entity.ArticleEntity;

public class ArticleAndTradeMembershipIdDto implements Dto {

	private ArticleEntity article;
	private Integer tradeMembershipId;

	public ArticleAndTradeMembershipIdDto(ArticleEntity article, Integer tradeMembershipId) {
		this.tradeMembershipId = tradeMembershipId;
		this.article = article;
	}
	
	public ArticleEntity getArticle() {
		return article;
	}

	public Integer getTradeMembershipId() {
		return tradeMembershipId;
	}

}