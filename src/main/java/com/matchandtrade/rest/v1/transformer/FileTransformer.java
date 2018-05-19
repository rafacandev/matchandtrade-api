package com.matchandtrade.rest.v1.transformer;

import org.springframework.beans.BeanUtils;

import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.rest.v1.json.FileJson;

public class FileTransformer {
	
	// Utility classes should not have public constructors
	private FileTransformer() {}

	public static FileJson transform(FileEntity entity) {
		FileJson result = new FileJson();
		BeanUtils.copyProperties(entity, result);
		return result;
	}

}
