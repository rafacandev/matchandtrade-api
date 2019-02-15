package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.service.ArticleAttachmentService;
import com.matchandtrade.rest.service.AuthenticationService;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.rest.v1.linkassembler.AttachmentLinkAssembler;
import com.matchandtrade.rest.v1.transformer.AttachmentTransformer;
import com.matchandtrade.rest.v1.validator.ArticleAttachmentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/matchandtrade-api/v1/articles/{articleId}/attachments")
public class ArticleAttachmentController implements Controller {
	@Autowired
	AuthenticationService authenticationService;
	@Autowired
	private ArticleAttachmentService articleAttachmentService;
	@Autowired
	private ArticleAttachmentValidator articleAttachmentValidator;
	@Autowired
	private AttachmentLinkAssembler attachmentLinkAssembler;
	private AttachmentTransformer attachmentTransformer = new AttachmentTransformer();

	@PutMapping("/{attachmentId}")
	@ResponseStatus(OK)
	public void put(@PathVariable Integer articleId, @PathVariable UUID attachmentId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request
		articleAttachmentValidator.validatePut(articleId, attachmentId);
		// Delegate to service layer
		articleAttachmentService.create(articleId, attachmentId);
	}

	@PostMapping({"", "/"})
	@ResponseStatus(CREATED)
	public AttachmentJson post(@PathVariable Integer articleId, @RequestPart(name="file") MultipartFile multipartFile) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request
		articleAttachmentValidator.validatePost(articleId);
		// Delegate to service layer
		AttachmentEntity entity = articleAttachmentService.create(articleId, multipartFile);
		// Transform the response
		AttachmentJson response = attachmentTransformer.transform(entity);
		// Assemble links
		attachmentLinkAssembler.assemble(response);
		return response;
	}

	@GetMapping({"","/"})
	@ResponseStatus(OK)
	public SearchResult<AttachmentJson> get(@PathVariable Integer articleId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request
		articleAttachmentValidator.validateGet(articleId);
		// Delegate to service layer
		SearchResult<AttachmentEntity> searchResult = articleAttachmentService.findByArticleId(articleId);
		// Transform the response
		SearchResult<AttachmentJson> response = attachmentTransformer.transform(searchResult);
		// Assemble links
		attachmentLinkAssembler.assemble(response);
		return response;
	}
}
