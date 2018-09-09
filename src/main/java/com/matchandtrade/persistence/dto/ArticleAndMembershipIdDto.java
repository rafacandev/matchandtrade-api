package com.matchandtrade.persistence.dto;

import com.matchandtrade.persistence.entity.ArticleEntity;

public class ArticleAndMembershipIdDto implements Dto {

	private ArticleEntity article;
	private Integer membershipId;

	public ArticleAndMembershipIdDto(ArticleEntity article, Integer membershipId) {
		this.membershipId = membershipId;
		this.article = article;
	}
	
	public ArticleEntity getArticle() {
		return article;
	}

	public Integer getMembershipId() {
		return membershipId;
	}

}