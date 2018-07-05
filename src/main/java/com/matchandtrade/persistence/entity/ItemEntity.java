package com.matchandtrade.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class ItemEntity extends ArticleEntity {
	
	private String description;
	private Set<AttachmentEntity> attachments = new HashSet<>();

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemEntity other = (ItemEntity) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		return result;
	}

	public void setAttachments(Set<AttachmentEntity> attachments) {
		this.attachments = attachments;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
