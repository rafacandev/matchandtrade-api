package com.matchandtrade.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "file_tb") // 'file' is a reserved word in most databases, hence we are sufixing it with '_tb'
public class FileEntity implements com.matchandtrade.persistence.entity.Entity {

	private String contentType;
	private Integer fileId;
	private String originalName;
	private Set<EssenceEntity> essences = new HashSet<>();

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileEntity other = (FileEntity) obj;
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
		if (fileId == null) {
			if (other.fileId != null)
				return false;
		} else if (!fileId.equals(other.fileId))
			return false;
		if (originalName == null) {
			if (other.originalName != null)
				return false;
		} else if (!originalName.equals(other.originalName))
			return false;
		return true;
	}

	@Column(name = "content_type", length = 128, nullable = true, unique = false)
	public String getContentType() {
		return contentType;
	}
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinTable(name="file_to_essence", joinColumns=@JoinColumn(name="file_id", foreignKey=@ForeignKey(name="file_to_essence_file_id_fk")), inverseJoinColumns = @JoinColumn(name="essence_id", foreignKey=@ForeignKey(name="file_to_essence_essence_id_fk")))
	public Set<EssenceEntity> getEssences() {
		return essences;
	}
	
	@Id
	@Column(name = "file_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getFileId() {
		return fileId;
	}

	// Most file systems limit filenames to 255 in length
	@Column(name = "original_name", length = 255, nullable = true, unique = false)
	public String getOriginalName() {
		return originalName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((essences == null) ? 0 : essences.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		result = prime * result + ((originalName == null) ? 0 : originalName.hashCode());
		return result;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setEssences(Set<EssenceEntity> essences) {
		this.essences = essences;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

}
