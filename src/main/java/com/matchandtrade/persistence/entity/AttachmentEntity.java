package com.matchandtrade.persistence.entity;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;

@Entity
@Table(name = "attachment")
public class AttachmentEntity implements com.matchandtrade.persistence.entity.Entity {

	private UUID attachmentId;
	private String contentType;
	private String name;
	private Set<EssenceEntity> essences = new LinkedHashSet<>();

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttachmentEntity other = (AttachmentEntity) obj;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		if (essences == null) {
			if (other.essences != null)
				return false;
		} else if (!essences.equals(other.essences))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Column(name = "content_type", length = 150, nullable = true, unique = false)
	public String getContentType() {
		return contentType;
	}
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinTable(name="attachment_to_essence", joinColumns=@JoinColumn(name="attachment_id", foreignKey=@ForeignKey(name="attachment_to_essence_attachment_id_fk")), inverseJoinColumns = @JoinColumn(name="essence_id", foreignKey=@ForeignKey(name="attachment_to_essence_essence_id_fk")))
	public Set<EssenceEntity> getEssences() {
		return essences;
	}
	
	@Id
	@Column(name = "attachment_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public UUID getAttachmentId() {
		return attachmentId;
	}

	// Most file systems limit filenames to 255 in length
	@Column(name = "name", length = 255, nullable = true, unique = false)
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((essences == null) ? 0 : essences.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setEssences(Set<EssenceEntity> essences) {
		this.essences = essences;
	}

	public void setAttachmentId(UUID attachmentId) {
		this.attachmentId = attachmentId;
	}

	public void setName(String name) {
		this.name = name;
	}

}
