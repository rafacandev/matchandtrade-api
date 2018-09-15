package com.matchandtrade.rest.v1.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.AttachmentService;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.rest.v1.link.AttachmentLinkAssember;
import com.matchandtrade.rest.v1.transformer.AttachmentTransformer;
import com.matchandtrade.rest.v1.validator.AttachmentValidator;
import com.matchandtrade.rest.v1.validator.FileValidator;

@RestController
@RequestMapping(path="/matchandtrade-api/v1/attachments")
public class AttachmentController implements Controller {

	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private AttachmentValidator attachmentValidator;
	@Autowired
	AuthenticationProvider authenticationProvider;

	@RequestMapping(path="/", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public AttachmentJson post(@RequestPart(required=true, name="file") MultipartFile multipartFile) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		FileValidator.validatePost(multipartFile);
		// Transform the request - nothing to transform
		// Delegate to service layer
		AttachmentEntity entity = attachmentService.create(multipartFile);
		// Transform the response
		AttachmentJson response = AttachmentTransformer.transform(entity);
		// Assemble links
		AttachmentLinkAssember.assemble(response, entity);
		return response;
	}

	@RequestMapping(path="/{attachmentId}", method=RequestMethod.GET)
	public AttachmentJson get(@PathVariable Integer attachmentId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		attachmentValidator.validateGet(attachmentId);
		// Delegate to service layer
		AttachmentEntity entity = attachmentService.get(attachmentId);
		// Transform the response
		AttachmentJson response = AttachmentTransformer.transform(entity);
		// Assemble links
		AttachmentLinkAssember.assemble(response, entity);
		return response;
	}
	
}
