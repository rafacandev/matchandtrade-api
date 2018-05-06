package com.matchandtrade.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "file_tb") // 'file' is a reserved word in most databases, hence we prefix it with '_tb'
public class FileEntity implements com.matchandtrade.persistence.entity.Entity {

	private Integer fileId;
	private String originalName;
	private String relativePath;

	@Id
	@Column(name = "file_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	// Most file systems limit filenames to 255 in length
	@Column(name = "original_name", length = 255, nullable = true, unique = false)
	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	// Most file systems limits paths to ~3000. Choosing 500 arbitrarily which seems enough for our needs
	@Column(name = "relative_path", length = 500, nullable = true, unique = false)
	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

}
