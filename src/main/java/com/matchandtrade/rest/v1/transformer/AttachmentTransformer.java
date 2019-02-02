package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.v1.json.AttachmentJson;

public class AttachmentTransformer extends Transformer<AttachmentEntity, AttachmentJson> {
	@Override
	public AttachmentJson transform(AttachmentEntity entity) {
		AttachmentJson result = new AttachmentJson();
		result.setContentType(entity.getContentType());
		result.setAttachmentId(entity.getAttachmentId());
		result.setName(entity.getName());
		return result;
	}

	@Override
	public AttachmentEntity transform(AttachmentJson json) {
		AttachmentEntity result = new AttachmentEntity();
		result.setContentType(json.getContentType());
		result.setAttachmentId(json.getAttachmentId());
		result.setName(json.getName());
		return result;
	}
}
