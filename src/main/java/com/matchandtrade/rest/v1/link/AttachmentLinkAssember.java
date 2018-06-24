package com.matchandtrade.rest.v1.link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import com.matchandtrade.config.MvcConfiguration;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.service.AttachmentService;
import com.matchandtrade.rest.v1.controller.AttachmentController;
import com.matchandtrade.rest.v1.json.AttachmentJson;

@Component
public class AttachmentLinkAssember {
	
	@Autowired
	private AttachmentService attachmentService;
	private static final String FILES_URL_PATTERN = MvcConfiguration.ESSENCES_URL_PATTERN.replace("*", "");
	
	public static void assemble(AttachmentJson json, AttachmentEntity entity) {
		if (json != null) {
			json.getLinks().add(linkTo(methodOn(AttachmentController.class).get(json.getAttachmentId())).withSelfRel());
		}
		
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
