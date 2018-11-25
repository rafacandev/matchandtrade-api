package com.matchandtrade.persistence.entity;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name="offer")
public class OfferEntity implements com.matchandtrade.persistence.entity.Entity {

	private Integer offerId;
	private ArticleEntity offeredArticle;
	private ArticleEntity wantedArticle;

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

	@OneToOne
	@JoinColumn(name="offered_article_id", foreignKey = @ForeignKey(name = "offer_offered_article_id_fk"))
	public ArticleEntity getOfferedArticle() {
		return offeredArticle;
	}
	
	@Id
	@Column(name = "offer_id")
	@SequenceGenerator(name="offer_id_generator", sequenceName = "offer_id_sequence")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "offer_id_generator")
	public Integer getOfferId() {
		return offerId;
	}

	@OneToOne
	@JoinColumn(name="wanted_article_id", foreignKey = @ForeignKey(name = "offer_wanted_article_id_fk"))
	public ArticleEntity getWantedArticle() {
		return wantedArticle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((offeredArticle == null) ? 0 : offeredArticle.hashCode());
		result = prime * result + ((wantedArticle == null) ? 0 : wantedArticle.hashCode());
		return result;
	}

	public void setOfferedArticle(ArticleEntity offeredArticle) {
		this.offeredArticle = offeredArticle;
	}

	public void setOfferId(Integer articleId) {
		this.offerId = articleId;
	}

	public void setWantedArticle(ArticleEntity wantedArticle) {
		this.wantedArticle = wantedArticle;
	}
	
}
