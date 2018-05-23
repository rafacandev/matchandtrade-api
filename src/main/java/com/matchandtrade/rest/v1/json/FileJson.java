package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.JsonLinkSupport;

public class FileJson extends JsonLinkSupport {

	private Integer fileId;
	private String originalName;
	private String contentType;

	public String getContentType() {
		return contentType;
	}

	public Integer getFileId() {
		return fileId;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

}
