package com.matchandtrade.rest.v1.link;

import org.springframework.hateoas.Link;

import com.matchandtrade.config.MvcConfiguration;
import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.rest.v1.json.FileJson;

public class FileLinkAssember {
	
	// Utility classes, which are a collection of static members, are not meant to be instantiated. Hence, at least one non-public constructor should be defined.
	private FileLinkAssember() {}

	public static void assemble(FileJson json, FileEntity entity) {
		String filesUrlPattern = MvcConfiguration.FILES_URL_PATTERN.replace("*", "");
		entity.getEssences().forEach(v -> {
			Link link = new Link(filesUrlPattern + v.getRelativePath(), v.getType().toString().toLowerCase());
			json.getLinks().add(link);
		});
	}

}
