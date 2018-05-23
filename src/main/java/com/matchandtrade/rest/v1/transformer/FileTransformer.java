package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.rest.v1.json.FileJson;

public class FileTransformer {
	
	// Utility classes should not have public constructors
	private FileTransformer() {}

	public static FileJson transform(FileEntity entity) {
		FileJson result = new FileJson();
		result.setContentType(entity.getContentType());
		result.setFileId(entity.getFileId());
		result.setOriginalName(entity.getOriginalName());
		return result;
	}

}
