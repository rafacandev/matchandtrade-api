package com.matchandtrade.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="offer")
public class OfferEntity implements com.matchandtrade.persistence.entity.Entity {

	private Integer offerId;
	private ArticleEntity offeredArticle;
	private ArticleEntity wantedArticle;
	
	@OneToOne
	@JoinColumn(name="offered_article_id")
	public ArticleEntity getOfferedArticle() {
		return offeredArticle;
	}

	@Id
	@Column(name="offer_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getOfferId() {
		return offerId;
	}

	@OneToOne
	@JoinColumn(name="wanted_article_id")
	public ArticleEntity getWantedArticle() {
		return wantedArticle;
	}

	public void setOfferedArticle(ArticleEntity offeredArticle) {
		this.offeredArticle = offeredArticle;
	}

	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
	}

	public void setWantedArticle(ArticleEntity wantedArticle) {
		this.wantedArticle = wantedArticle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((offeredArticle == null) ? 0 : offeredArticle.hashCode());
		result = prime * result + ((wantedArticle == null) ? 0 : wantedArticle.hashCode());
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
		OfferEntity other = (OfferEntity) obj;
		if (offeredArticle == null) {
			if (other.offeredArticle != null)
				return false;
		} else if (!offeredArticle.equals(other.offeredArticle))
			return false;
		if (wantedArticle == null) {
			if (other.wantedArticle != null)
				return false;
		} else if (!wantedArticle.equals(other.wantedArticle))
			return false;
		return true;
	}
	
}
