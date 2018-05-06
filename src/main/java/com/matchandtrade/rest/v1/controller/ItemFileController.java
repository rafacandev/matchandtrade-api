package com.matchandtrade.rest.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.FileStorageService;
import com.matchandtrade.rest.service.ItemFileService;
import com.matchandtrade.rest.v1.validator.ItemFileValidator;

@RestController
@RequestMapping(path = "/matchandtrade-web-api/v1/trade-memberships")
public class ItemFileController implements Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemFileController.class);

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	private ItemFileValidator itemFileValidator;
	@Autowired
	private ItemFileService itemFileService;
	@Autowired
	private FileStorageService fileService;
	
	

	@GetMapping("/{filename}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		LOGGER.debug("Requesting file with name: {}", filename);
		Resource file = fileService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@PostMapping("/{tradeMembershipId}/items/{itemId}/files")
	@ResponseStatus(code = HttpStatus.CREATED)
	public void post(@PathVariable Integer tradeMembershipId, @PathVariable Integer itemId,	@RequestParam("file") MultipartFile file) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		itemFileValidator.validatePost(authenticationProvider.getAuthentication().getUser().getUserId(), tradeMembershipId);
		
		// Transform the request - nothing to transform
		// Delegate to service layer
		itemFileService.create(tradeMembershipId, itemId, file);

		
		LOGGER.info("Uploading filename: {}", file.getName());
//		fileService.store(file);
	}

}