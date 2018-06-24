package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.JsonLinkSupport;

public class AttachmentJson extends JsonLinkSupport {

	private Integer attachmentId;
	private String name;
	private String contentType;

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public String getContentType() {
		return contentType;
	}

	public String getName() {
		return name;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setName(String name) {
		this.name = name;
	}

}
