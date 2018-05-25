package com.matchandtrade.rest.v1.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.FileService;
import com.matchandtrade.rest.v1.json.FileJson;
import com.matchandtrade.rest.v1.link.FileLinkAssember;
import com.matchandtrade.rest.v1.transformer.FileTransformer;

@RestController
@RequestMapping(path="/matchandtrade-web-api/v1/files")
public class FileController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	private FileService fileService;

	@RequestMapping(path="/", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public FileJson post(MultipartFile file) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request - TODO: Add some sort of file upload quota
		// Transform the request - nothing to transform
		// Delegate to service layer
		FileEntity entity = fileService.create(file);
		// Transform the response
		FileJson response = FileTransformer.transform(entity);
		// Assemble links
		FileLinkAssember.assemble(response, entity);
		return response;
	}
	
}
