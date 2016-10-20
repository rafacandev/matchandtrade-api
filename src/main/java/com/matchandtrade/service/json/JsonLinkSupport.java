package com.matchandtrade.service.json;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

public abstract class JsonLinkSupport implements Json {
	
	private Set<JsonLink> _links = new HashSet<JsonLink>();

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
//	@ApiModelProperty(hidden=true)
	public Set<JsonLink> get_links() {
		return _links;
	}

	public void set_links(Set<JsonLink> _links) {
		this._links = _links;
	}
}
