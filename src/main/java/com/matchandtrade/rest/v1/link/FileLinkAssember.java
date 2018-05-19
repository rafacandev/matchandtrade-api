package com.matchandtrade.rest.v1.link;

import org.springframework.hateoas.Link;

import com.matchandtrade.rest.v1.json.FileJson;

public class FileLinkAssember {
	
	// Utility classes, which are a collection of static members, are not meant to be instantiated. Hence, at least one non-public constructor should be defined.
	private FileLinkAssember() {}

	public static void assemble(FileJson response) {
		String path = "files/" + response.getRelativePath();
		Link link = new Link(path, "file");
		response.getLinks().add(link);
	}

}
