package com.matchandtrade.persistence.entity;

import java.util.Set;

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
	private String description;
	private String name;
	private Set<FileEntity> files;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemEntity other = (ItemEntity) obj;
		if (itemId == null) {
			if (other.itemId != null)
				return false;
		} else if (!itemId.equals(other.itemId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Column(name = "description", length = 500, nullable = true, unique = false)
	public String getDescription() {
		return description;
	}

	@OneToMany
	@JoinTable(name = "item_to_file", 
		joinColumns = @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "item_to_file_item_id_fk")),
		inverseJoinColumns = @JoinColumn(name = "file_id", foreignKey = @ForeignKey(name = "item_to_file_file_id_fk")))
	public Set<FileEntity> getFiles() {
		return files;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFiles(Set<FileEntity> files) {
		this.files = files;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public void setName(String name) {
		this.name = name;
	}

}
