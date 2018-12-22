package com.matchandtrade.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * POJO to support HATEROAS with links
 */
@JsonInclude(value=Include.NON_EMPTY)
public class JsonLinkSupport implements Json {
	private List<Map.Entry<String, String>> links = new ArrayList<>();

	@JsonProperty("_links")
	public List<Map.Entry<String, String>> getLinks() {
		return links;
	}

	public void add(String rel, String href) {
		links.add(new AbstractMap.SimpleEntry(rel, href));
	}
}
