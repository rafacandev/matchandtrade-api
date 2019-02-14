package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
public class AttachmentValidator {
	@Autowired
	AttachmentService attachmentService;

	public void validateGet(UUID attachmentId) {
		AttachmentEntity attachment = attachmentService.findByAttachmentId(attachmentId);
		if (attachment == null) {
			throw new RestException(NOT_FOUND, "Attachment.attachmentId was not found");
		}
	}

	public void validatePost(MultipartFile multipartFile) {
		int megabyteMultiplier = 1_000_000;
		if (multipartFile.getSize() > 5 * megabyteMultiplier) {
			throw new RestException(BAD_REQUEST, "Files need to be smaller than 5 megabytes");
		}
	}
}
