package com.matchandtrade.rest.v1.link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.Link;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.rest.v1.controller.ArticleController;
import com.matchandtrade.rest.v1.controller.ArticleAttachmentController;
import com.matchandtrade.rest.v1.json.ArticleJson;

public class ArticleLinkAssember {
	
	// Utility classes, which are a collection of static members, are not meant to be instantiated. Hence, at least one non-public constructor should be defined.
	private ArticleLinkAssember() {}

	private static Set<Link> buildLink(Integer membershipId, Integer articleId) {
		Set<Link> result = new HashSet<>();
		result.add(linkTo(methodOn(ArticleController.class).get(membershipId, articleId)).withSelfRel());
		result.add(linkTo(methodOn(ArticleAttachmentController.class).get(membershipId, articleId, null, null)).withRel("attachments"));
		return result;
	}
	
	public static void assemble(ArticleJson json, Integer membershipId) {
		if (json != null) {
			json.getLinks().addAll(buildLink(membershipId, json.getArticleId()));
		}
	}

	public static void assemble(SearchResult<ArticleJson> response, Integer membershipId) {
		for (ArticleJson i : response.getResultList()) {
			assemble(i, membershipId);
		}
	}

}
