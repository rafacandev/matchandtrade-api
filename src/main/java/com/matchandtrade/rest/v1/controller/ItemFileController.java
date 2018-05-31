package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.FileService;
import com.matchandtrade.rest.service.ItemFileService;
import com.matchandtrade.rest.v1.json.FileJson;
import com.matchandtrade.rest.v1.link.FileLinkAssember;
import com.matchandtrade.rest.v1.transformer.FileTransformer;
import com.matchandtrade.rest.v1.validator.ItemFileValidator;

@RestController
@RequestMapping(path = "/matchandtrade-web-api/v1/trade-memberships")
public class ItemFileController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	private ItemFileValidator itemFileValidator;
	@Autowired
	private ItemFileService itemFileService;
	@Autowired
	private FileService fileService;
	@Autowired
	private FileLinkAssember fileLinkAssembler;

	@PostMapping("/{tradeMembershipId}/items/{itemId}/files/{fileId}")
	@ResponseStatus(HttpStatus.CREATED)
	public FileJson post(@PathVariable Integer tradeMembershipId, @PathVariable Integer itemId, @PathVariable Integer fileId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		itemFileValidator.validatePost(authenticationProvider.getAuthentication().getUser().getUserId(), tradeMembershipId, itemId);
		// Transform the request
		FileEntity fileEntity = fileService.get(fileId);
		// Delegate to service layer
		itemFileService.addFileToItem(itemId, fileId);
		// Transform the response
		FileJson response = FileTransformer.transform(fileEntity);
		// Assemble links
		FileLinkAssember.assemble(response, fileEntity);
		return response;
	}

	@RequestMapping(path={"/{tradeMembershipId}/items/{itemId}/files", "/{tradeMembershipId}/items/{itemId}/files/"}, method=RequestMethod.GET)
	public SearchResult<FileJson> get(@PathVariable Integer tradeMembershipId, @PathVariable Integer itemId, Integer _pageNumber, Integer _pageSize) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		itemFileValidator.validateGet(authenticationProvider.getAuthentication().getUser().getUserId(), tradeMembershipId, _pageNumber, _pageSize);
		// Delegate to service layer
		SearchResult<FileEntity> searchResult = itemFileService.search(itemId, _pageNumber, _pageSize);
		// Transform the response
		SearchResult<FileJson> response = FileTransformer.transform(searchResult);
		// Assemble links
		fileLinkAssembler.assemble(response);
		return response;
	}

}