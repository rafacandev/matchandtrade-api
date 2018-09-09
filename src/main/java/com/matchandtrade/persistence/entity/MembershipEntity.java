package com.matchandtrade.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "membership")
public class MembershipEntity implements com.matchandtrade.persistence.entity.Entity {
	
	public enum Type {
		OWNER, MEMBER
	}
	
	private Set<ArticleEntity> articles = new HashSet<>();
	private Set<OfferEntity> offers = new HashSet<>();
	private TradeEntity trade;
	private Integer membershipId;
	private Type type;
	private UserEntity user;

	@OneToMany
	@JoinTable(name="membership_to_article", joinColumns=@JoinColumn(name="membership_id", foreignKey=@ForeignKey(name="membership_to_article_membership_id_fk")), inverseJoinColumns = @JoinColumn(name="article_id", foreignKey=@ForeignKey(name="membership_to_article_article_id_fk")))
	public Set<ArticleEntity> getArticles() {
		return articles;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name="membership_to_offer", joinColumns=@JoinColumn(name="membership_id", foreignKey=@ForeignKey(name="membership_to_offer_membership_id_fk")), inverseJoinColumns=@JoinColumn(name="offer_id", foreignKey=@ForeignKey(name="membership_to_offer_offer_id_fk")))
	public Set<OfferEntity> getOffers() {
		return offers;
	}

	@OneToOne
	@JoinColumn(name="trade_id", foreignKey=@ForeignKey(name="membership_trade_id_fk"))
	public TradeEntity getTrade() {
		return trade;
	}

	@Id
	@Column(name="membership_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getMembershipId() {
		return membershipId;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="type", nullable=false)
	public Type getType() {
		return type;
	}

	@OneToOne
	@JoinColumn(name="user_id", foreignKey=@ForeignKey(name="membership_user_id_fk"))
	public UserEntity getUser() {
		return user;
	}

	public void setArticles(Set<ArticleEntity> articles) {
		this.articles = articles;
	}

	public void setOffers(Set<OfferEntity> offers) {
		this.offers = offers;
	}

	public void setTrade(TradeEntity trade) {
		this.trade = trade;
	}

	public void setMembershipId(Integer membershipId) {
		this.membershipId = membershipId;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}
