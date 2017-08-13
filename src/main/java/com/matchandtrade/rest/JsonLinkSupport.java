package com.matchandtrade.rest;

import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value=Include.NON_EMPTY)
public class JsonLinkSupport implements Json {

	private Set<Link> links = new HashSet<>();

	@JsonProperty("_links")
	public Set<Link> getLinks() {
		return links;
	}

	public void setLinks(Set<Link> links) {
		this.links = links;
	}

}
