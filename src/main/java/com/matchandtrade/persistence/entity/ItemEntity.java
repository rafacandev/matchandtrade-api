package com.matchandtrade.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class ItemEntity implements com.matchandtrade.persistence.entity.Entity {
	
	private Integer itemId;
	private String name;
	private Set<WantItemEntity> wantItems = new HashSet<>();

	@Id
	@Column(name = "item_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getItemId() {
		return itemId;
	}

	@Column(name = "name", length = 150, nullable = false, unique = false)
	public String getName() {
		return name;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "item_to_want_item", joinColumns = @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "item_to_want_item_item_id_fk")), inverseJoinColumns = @JoinColumn(name = "want_item_id", foreignKey = @ForeignKey(name = "item_to_want_item_want_item_id_fk")))
	public Set<WantItemEntity> getWantItems() {
		return wantItems;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWantItems(Set<WantItemEntity> wantItems) {
		this.wantItems = wantItems;
	}

}
