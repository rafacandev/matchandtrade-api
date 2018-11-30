package com.matchandtrade.rest.v1.json;

import com.matchandtrade.rest.JsonLinkSupport;

import java.util.Objects;

// TODO: Add essences
public class AttachmentJson extends JsonLinkSupport {
	private Integer attachmentId;
	private String name;
	private String contentType;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AttachmentJson that = (AttachmentJson) o;
		return Objects.equals(attachmentId, that.attachmentId) &&
			Objects.equals(name, that.name) &&
			Objects.equals(contentType, that.contentType);
	}

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public String getContentType() {
		return contentType;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(attachmentId, name, contentType);
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
