package com.matchandtrade.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "want_list")
public class WantListEntity implements com.matchandtrade.persistence.entity.Entity {

	private Integer wantListId;
	private ItemEntity offer;
	private List<ItemEntity> wants = new ArrayList<>(); // It could be a Set however it has been implemented as list to support 'priorities' in the future.

	@OneToOne
	@JoinColumn(name="offer_item_id", foreignKey = @ForeignKey(name = "want_list__offer_item_id_fk"))
	public ItemEntity getOffer() {
		return offer;
	}

	@Id
	@Column(name = "want_list_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getWantListId() {
		return wantListId;
	}

	@OneToMany
	@JoinTable(name = "want_list_to_item", joinColumns = @JoinColumn(name = "want_list_id", foreignKey=@ForeignKey(name="want_list_to_item__want_list_id_fk")), inverseJoinColumns = @JoinColumn(name = "item_id", foreignKey=@ForeignKey(name="want_list_to_item__item_id_fk")))
	public List<ItemEntity> getWants() {
		return wants;
	}

	public void setOffer(ItemEntity offer) {
		this.offer = offer;
	}

	public void setWantListId(Integer wantListId) {
		this.wantListId = wantListId;
	}

	public void setWants(List<ItemEntity> wants) {
		this.wants = wants;
	}

}
