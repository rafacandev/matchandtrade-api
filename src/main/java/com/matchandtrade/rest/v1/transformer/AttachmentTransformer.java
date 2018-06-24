package com.matchandtrade.rest.v1.transformer;

import java.util.List;
import java.util.stream.Collectors;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.v1.json.AttachmentJson;

public class AttachmentTransformer {
	
	// Utility classes should not have public constructors
	private AttachmentTransformer() {}

	public static AttachmentJson transform(AttachmentEntity entity) {
		AttachmentJson result = new AttachmentJson();
		result.setContentType(entity.getContentType());
		result.setAttachmentId(entity.getAttachmentId());
		result.setName(entity.getName());
		return result;
	}

	public static SearchResult<AttachmentJson> transform(SearchResult<AttachmentEntity> searchResult) {
		List<AttachmentJson> attachments = searchResult.getResultList().stream().map(AttachmentTransformer::transform).collect(Collectors.toList());
		return new SearchResult<>(attachments, searchResult.getPagination());
	}

}
