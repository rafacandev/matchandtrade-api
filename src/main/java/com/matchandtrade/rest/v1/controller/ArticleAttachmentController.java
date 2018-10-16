package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@RequestMapping(path = "/matchandtrade-api/v1/memberships")
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

	@PostMapping("/articles/{articleId}/attachments/{attachmentId}")
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
	
	@DeleteMapping("/articles/{articleId}/attachments/{attachmentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer articleId, @PathVariable Integer attachmentId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		articleAttachmentValidator.validateDelete(authenticationProvider.getAuthentication().getUser().getUserId(), articleId, attachmentId);
		// Delegate to service layer
		articleAttachmentService.deleteAttachmentFromArticle(articleId, attachmentId);
	}


	@RequestMapping(path={"/{membershipId}/articles/{articleId}/attachments", "/{membershipId}/articles/{articleId}/attachments/"}, method=RequestMethod.GET)
	public SearchResult<AttachmentJson> get(@PathVariable Integer membershipId, @PathVariable Integer articleId, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		articleAttachmentValidator.validateGet(authenticationProvider.getAuthentication().getUser().getUserId(), membershipId, _pageNumber, _pageSize);
		// Delegate to service layer
		SearchResult<AttachmentEntity> searchResult = articleAttachmentService.search(articleId, _pageNumber, _pageSize);
		// Transform the response
		SearchResult<AttachmentJson> response = AttachmentTransformer.transform(searchResult);
		// Assemble links
		attachmentLinkAssembler.assemble(response);
		return response;
	}

}