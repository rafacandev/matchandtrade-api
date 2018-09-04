package com.matchandtrade.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "item")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ItemEntity extends ArticleEntity {

	private String description;
	private String name;
	private Set<AttachmentEntity> attachments = new HashSet<>();

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemEntity other = (ItemEntity) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@OneToMany
	@JoinTable(name = "item_to_attachment", 
		joinColumns = @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "item_to_attachment_item_id_fk")),
		inverseJoinColumns = @JoinColumn(name = "attachment_id", foreignKey = @ForeignKey(name = "item_to_attachment_attachment_id_fk")))
	public Set<AttachmentEntity> getAttachments() {
		return attachments;
	}


	@Column(name = "description", length = 500, nullable = true, unique = false)
	public String getDescription() {
		return description;
	}

	@Column(name = "name", length = 150, nullable = false, unique = false)
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public void setAttachments(Set<AttachmentEntity> attachments) {
		this.attachments = attachments;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
