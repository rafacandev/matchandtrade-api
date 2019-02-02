package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.service.AttachmentService;
import com.matchandtrade.rest.service.AuthenticationService;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.rest.v1.linkassembler.AttachmentLinkAssembler;
import com.matchandtrade.rest.v1.transformer.AttachmentTransformer;
import com.matchandtrade.rest.v1.validator.AttachmentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/matchandtrade-api/v1/attachments")
public class AttachmentController implements Controller {
	@Autowired
	AuthenticationService authenticationService;
	@Autowired
	private AttachmentLinkAssembler attachmentLinkAssembler;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private AttachmentValidator attachmentValidator;
	private AttachmentTransformer attachmentTransformer = new AttachmentTransformer();

	@GetMapping("/{attachmentId}")
	public AttachmentJson get(@PathVariable UUID attachmentId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request
		attachmentValidator.validateGet(attachmentId);
		// Delegate to service layer
		AttachmentEntity entity = attachmentService.findByAttachmentId(attachmentId);
		// Transform the response
		AttachmentJson response = attachmentTransformer.transform(entity);
		// Assemble links
		attachmentLinkAssembler.assemble(response);
		return response;
	}
}