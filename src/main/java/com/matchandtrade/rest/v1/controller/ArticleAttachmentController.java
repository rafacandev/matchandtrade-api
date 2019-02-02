package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.service.ArticleAttachmentService;
import com.matchandtrade.rest.service.AuthenticationService;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.validator.ArticleAttachmentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

	@PutMapping("/{attachmentId}")
	@ResponseStatus(CREATED)
	public void put(@PathVariable Integer articleId, @PathVariable UUID attachmentId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request
		articleAttachmentValidator.validatePut(articleId, attachmentId);
		// Delegate to service layer
		articleAttachmentService.create(articleId, attachmentId);
	}

	@GetMapping("/")
	@ResponseStatus(OK)
	public void get(@PathVariable Integer articleId) {
		// Validate the request
		articleAttachmentValidator.validateGet(articleId);
		// Delegate to service layer
		SearchResult<AttachmentEntity> searchResult = articleAttachmentService.findByArticleId(articleId);
	}
}
