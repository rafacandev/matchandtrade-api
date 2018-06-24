package com.matchandtrade.rest.v1.link;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import com.matchandtrade.config.MvcConfiguration;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.service.AttachmentService;
import com.matchandtrade.rest.v1.json.AttachmentJson;

@Component
public class AttachmentLinkAssember {
	
	@Autowired
	private AttachmentService attachmentService;
	private static final String FILES_URL_PATTERN = MvcConfiguration.FILES_URL_PATTERN.replace("*", "");
	
	// Utility classes, which are a collection of static members, are not meant to be instantiated. Hence, at least one non-public constructor should be defined.
	private AttachmentLinkAssember() {}

	public static void assemble(AttachmentJson json, AttachmentEntity entity) {
		// TODO: add self link when FileController.get(id) is implemented
		entity.getEssences().forEach(v -> {
			Link link = new Link(FILES_URL_PATTERN + v.getRelativePath(), v.getType().toString().toLowerCase());
			json.getLinks().add(link);
		});
	}

	public void assemble(SearchResult<AttachmentJson> response) {
		for (AttachmentJson fileJson : response.getResultList()) {
			AttachmentEntity fileEntity = attachmentService.get(fileJson.getAttachmentId());
			assemble(fileJson, fileEntity);
		}
	}

}
