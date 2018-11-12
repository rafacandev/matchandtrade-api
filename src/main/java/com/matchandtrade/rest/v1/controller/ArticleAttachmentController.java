package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.v1.validator.AttachmentVerificator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.AttachmentService;
import com.matchandtrade.rest.service.ArticleAttachmentService;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.rest.v1.link.AttachmentLinkAssember;
import com.matchandtrade.rest.v1.transformer.AttachmentTransformer;
import com.matchandtrade.rest.v1.validator.ArticleAttachmentValidator;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/matchandtrade-api/v1")
public class ArticleAttachmentController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	private ArticleAttachmentValidator articleAttachmentValidator;
	@Autowired
	private ArticleAttachmentService articleAttachmentService;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private AttachmentLinkAssember attachmentLinkAssembler;

	@PostMapping("/articles/{articleId}/attachments/{attachmentId}/")
	@ResponseStatus(HttpStatus.CREATED)
	public AttachmentJson post(@PathVariable Integer articleId, @PathVariable Integer attachmentId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		articleAttachmentValidator.validatePost(authenticationProvider.getAuthentication().getUser().getUserId(), articleId);
		// Transform the request
		AttachmentEntity attachmentEntity = attachmentService.get(attachmentId);
		// Delegate to service layer
		articleAttachmentService.addAttachmentToArticle(articleId, attachmentId);
		// Transform the response
		AttachmentJson response = AttachmentTransformer.transform(attachmentEntity);
		// Assemble links
		AttachmentLinkAssember.assemble(response, attachmentEntity);
		return response;
	}
	
	@DeleteMapping("/articles/{articleId}/attachments/{attachmentId}/")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer articleId, @PathVariable Integer attachmentId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		articleAttachmentValidator.validateDelete(authenticationProvider.getAuthentication().getUser().getUserId(), articleId, attachmentId);
		// Delegate to service layer
		articleAttachmentService.deleteAttachmentFromArticle(articleId, attachmentId);
	}

	@Autowired
	private AttachmentVerificator attachmentVerificator;

	@PostMapping(path="/articles/{attachmentId}/attachments/")
	@ResponseStatus(HttpStatus.CREATED)
	public AttachmentJson post(
			@PathVariable Integer articleId,
			@RequestPart(required=true, name="file") MultipartFile multipartFile) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		attachmentVerificator.validatePost(authenticationProvider.getAuthentication().getUser().getUserId(), articleId, multipartFile);
		// Transform the request - nothing to transform
		// Delegate to service layer
		AttachmentEntity attachment = articleAttachmentService.create(articleId, multipartFile);
		// TODO Assemble links
		return AttachmentTransformer.transform(attachment);
	}

}