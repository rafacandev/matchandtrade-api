package com.matchandtrade.rest;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JSON class which extends <i>RestResponseInterceptor</i> and provide support to Spring HATEOAS. 
 * 
 * @author rafael.santos.bra@gmail.com
 * @see com.matchandtrade.config.RestResponseInterceptor;
 */
public abstract class JsonLinkSupport extends ResourceSupport implements Json {
	
	public abstract void buildLinks();
	
	@JsonProperty("_links")
	@JsonInclude(value=Include.NON_EMPTY)
	@Override
	public List<Link> getLinks() {
		return super.getLinks();
	}
	
}
