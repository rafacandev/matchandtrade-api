package com.matchandtrade.persistence.entity;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "essence") // 'file' is a reserved word in most databases, hence we prefix it with '_tb'
public class EssenceEntity implements com.matchandtrade.persistence.entity.Entity {

	public enum Type {
		THUMBNAIL("THUMBNAIL"), ORIGINAL("ORIGINAL");
		private String text;

		private Type(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
	}

	private Type type;
	private Integer essenceId;
	private String relativePath;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EssenceEntity other = (EssenceEntity) obj;
		if (relativePath == null) {
			if (other.relativePath != null)
				return false;
		} else if (!relativePath.equals(other.relativePath))
			return false;
		return true;
	}

	@Id
	@Column(name = "essence_id")
	@SequenceGenerator(name="essence_id_generator", sequenceName = "essence_id_sequence")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "essence_id_generator")
	public Integer getEssenceId() {
		return essenceId;
	}

	@Column(name = "relative_path", length = 500, nullable = true, unique = false)
	public String getRelativePath() {
		return relativePath;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="type", nullable=false, length = 100)
	public Type getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((relativePath == null) ? 0 : relativePath.hashCode());
		return result;
	}

	public void setEssenceId(Integer essenceId) {
		this.essenceId = essenceId;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
