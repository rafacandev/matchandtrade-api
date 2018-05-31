package com.matchandtrade.rest.v1.validator;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import com.matchandtrade.rest.RestException;

public class FileValidator {
	
	// TODO: Add some sort of file upload quota
	public static void validatePost(MultipartFile file) {
		if (file == null || file.getSize() < 1) {
			throw new RestException(HttpStatus.BAD_REQUEST, "File cannot be empty");
		}
		
	}
	
}
