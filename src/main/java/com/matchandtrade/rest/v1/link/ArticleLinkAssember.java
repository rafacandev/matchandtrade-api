package com.matchandtrade.rest.v1.link;

import com.matchandtrade.rest.v1.json.ArticleJson;

public class ArticleLinkAssember {
	
	// Utility classes, which are a collection of static members, are not meant to be instantiated. Hence, at least one non-public constructor should be defined.
	private ArticleLinkAssember() {}

//	private static Set<Link> buildLink(Integer articleId) {
//		Set<Link> result = new HashSet<>();
//		result.add(linkTo(methodOn(ArticleController.class).get(tradeMembershipId, articleId)).withSelfRel());
//		result.add(linkTo(methodOn(ItemAttachmentController.class).get(tradeMembershipId, articleId, null, null)).withRel("attachments"));
//		return result;
//	}
//	
	public static void assemble(ArticleJson json) {
		if (json != null) {
//			json.getLinks().addAll(buildLink(json.getArticleId()));
		}
	}

//	public static void assemble(SearchResult<ItemJson> response, Integer tradeMembershipId) {
//		for (ItemJson i : response.getResultList()) {
//			assemble(i, tradeMembershipId);
//		}
//	}

}
