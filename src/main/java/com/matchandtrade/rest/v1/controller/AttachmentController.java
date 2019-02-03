package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.service.AttachmentService;
import com.matchandtrade.rest.service.AuthenticationService;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.rest.v1.linkassembler.AttachmentLinkAssembler;
import com.matchandtrade.rest.v1.transformer.AttachmentTransformer;
import com.matchandtrade.rest.v1.validator.AttachmentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

	@GetMapping()
	public SearchResult<AttachmentJson> get(Integer articleId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request
		attachmentValidator.validateFind(articleId);
		// Delegate to service layer
		SearchResult<AttachmentEntity> searchResult = attachmentService.findByArticleId(articleId);
		// Transform the response
		SearchResult<AttachmentJson> response = attachmentTransformer.transform(searchResult);
		// Assemble links
		attachmentLinkAssembler.assemble(response);
		return response;
	}

	@PostMapping(path = {"", "/"})
	@ResponseStatus(HttpStatus.CREATED)
	public AttachmentJson post(@RequestPart(name="file") MultipartFile multipartFile) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request
		attachmentValidator.validatePost(multipartFile);
		AttachmentEntity entity = attachmentService.create(multipartFile);
		// Transform the response
		AttachmentJson response = attachmentTransformer.transform(entity);
		// Assemble links
		attachmentLinkAssembler.assemble(response);
		return response;
	}
}