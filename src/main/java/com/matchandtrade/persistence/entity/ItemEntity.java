package com.matchandtrade.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class ItemEntity implements com.matchandtrade.persistence.entity.Entity {
	
	private Integer itemId;
	private String name;

	@Column(name = "name", length = 150, nullable = false, unique = false)
	public String getName() {
		return name;
	}

	@Id
	@Column(name = "item_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getItemId() {
		return itemId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

}
