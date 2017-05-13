package com.matchandtrade.rest.v1.json;

import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.matchandtrade.rest.Json;

public class JsonLinkable implements Json {

	private Set<Link> links = new HashSet<>();

	@JsonProperty("_links")
	@JsonInclude(value=Include.NON_EMPTY)
	public Set<Link> getLinks() {
		return links;
	}

	public void setLinks(Set<Link> links) {
		this.links = links;
	}

}
