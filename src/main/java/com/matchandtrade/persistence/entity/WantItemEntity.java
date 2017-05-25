package com.matchandtrade.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "want_item")
public class WantItemEntity implements com.matchandtrade.persistence.entity.Entity {
	
	private Integer wantItemId;
	private Integer priority;
	private ItemEntity item;

	@OneToOne
	@JoinColumn(name = "item_id", nullable = false, unique = false, foreignKey = @ForeignKey(name = "want_item_item_id_fk"))
	public ItemEntity getItem() {
		return item;
	}

	public Integer getPriority() {
		return priority;
	}

	@Id
	@Column(name = "want_item_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getWantItemId() {
		return wantItemId;
	}

	public void setItem(ItemEntity item) {
		this.item = item;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public void setWantItemId(Integer wantItemId) {
		this.wantItemId = wantItemId;
	}

}
