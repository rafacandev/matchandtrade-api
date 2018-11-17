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
		// So far nobody needs this method. Fell free to implement it when needed.
		throw new UnsupportedOperationException();
	}

}
