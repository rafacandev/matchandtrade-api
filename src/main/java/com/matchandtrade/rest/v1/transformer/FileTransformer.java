package com.matchandtrade.rest.v1.transformer;

import java.util.List;
import java.util.stream.Collectors;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.rest.v1.json.FileJson;

public class FileTransformer {
	
	// Utility classes should not have public constructors
	private FileTransformer() {}

	public static FileJson transform(FileEntity entity) {
		FileJson result = new FileJson();
		result.setContentType(entity.getContentType());
		result.setFileId(entity.getFileId());
		result.setName(entity.getName());
		return result;
	}

	public static SearchResult<FileJson> transform(SearchResult<FileEntity> searchResult) {
		List<FileJson> files = searchResult.getResultList().stream().map(FileTransformer::transform).collect(Collectors.toList());
		return new SearchResult<>(files, searchResult.getPagination());
	}

}
