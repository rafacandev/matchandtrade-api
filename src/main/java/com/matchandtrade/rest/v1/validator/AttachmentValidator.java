package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.rest.RestException;

@Component
public class AttachmentValidator {
	
	@Autowired
	private AttachmentRepositoryFacade attachmentRepositoryFacade;

	public void validateGet(Integer attachmentId) {
		// TODO: Should we validate which users can have access to files?
		AttachmentEntity attachment = attachmentRepositoryFacade.get(attachmentId);
		if (attachment == null) {
			throw new RestException(HttpStatus.NOT_FOUND);
		}
	}

}
