package com.matchandtrade.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "article")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ArticleEntity implements com.matchandtrade.persistence.entity.Entity {

	private Integer articleId;
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
		ArticleEntity other = (ArticleEntity) obj;
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

	@Id
	@Column(name = "article_id")
	@SequenceGenerator(name="article_id_generator", sequenceName = "article_id_sequence")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "article_id_generator")
	public Integer getArticleId() {
		return articleId;
	}

	@OneToMany
	@JoinTable(name = "article_to_attachment", 
		joinColumns = @JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "article_to_attachment_article_id_fk")),
		inverseJoinColumns = @JoinColumn(name = "attachment_id", foreignKey = @ForeignKey(name = "article_to_attachment_attachment_id_fk")))
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

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
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
