package com.matchandtrade.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="offer")
public class OfferEntity extends ArticleEntity {

	private ArticleEntity offeredArticle;
	private ArticleEntity wantedArticle;
	
	@OneToOne
	@JoinColumn(name="offered_article_id", foreignKey = @ForeignKey(name = "offer_offered_article_id_fk"))
	public ArticleEntity getOfferedArticle() {
		return offeredArticle;
	}

	@OneToOne
	@JoinColumn(name="wanted_article_id", foreignKey = @ForeignKey(name = "offer_wanted_article_id_fk"))
	public ArticleEntity getWantedArticle() {
		return wantedArticle;
	}

	public void setOfferedArticle(ArticleEntity offeredArticle) {
		this.offeredArticle = offeredArticle;
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
