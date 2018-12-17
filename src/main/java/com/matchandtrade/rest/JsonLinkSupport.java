package com.matchandtrade.rest;

import java.util.*;

import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO to support HATEROAS in HAL format.
 * {@see: https://tools.ietf.org/html/draft-kelly-json-hal-08}
 */
@JsonInclude(value=Include.NON_EMPTY)
public class JsonLinkSupport implements Json {
	private Map<String, Map.Entry<String, String>> links = new HashMap<>();

	@JsonProperty("_links")
	public Map<String, Map.Entry<String, String>> getLinks() {
		return links;
	}

	public void add(String rel, String href) {
		Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>("href", href);
		links.put(rel, entry);
	}

	public void add(Link link) {
		add(link.getRel(), link.getHref());
	}
}
