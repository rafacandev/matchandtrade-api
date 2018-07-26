package com.matchandtrade.rest.v1.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.json.ItemJson;

@Component
public class ArticleValidator {

	/**
	 * Throws RestException when: <br>
	 * <ul>
	 * 	<li>ArticleJson.type isn't ITEM</li>
	 * 	<li>ArticleJson.name isn't between 3-150 chars long</li>
	 * 	<li>ItemJson.description is longer than 500 chars long</li>
	 * </ul>
	 * 
	 * @param userId
	 * @param article
	 */
	public void validatePost(ArticleJson article) {
		verifyType(article);
		verifyName(article.getName());
		if (article instanceof ItemJson) {
			ItemJson item = (ItemJson) article;
			verifyItemDescription(item);
		}
	}

	private void verifyItemDescription(ItemJson item) {
		if (item.getDescription() != null && item.getDescription().length() > 500) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Item.description must be equal or less than 500 characters long.");
		}
	}

	private void verifyName(String name) {
		if (name == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Article name is mandatory.");
		}
		if (name.length() < 3) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Article name must be at least 3 characters long.");
		}
		if (name.length() > 150) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Article name must be equal or less than 150 characters long.");
		}
	}

	private void verifyType(ArticleJson article) {
		if (!(article instanceof ItemJson) || article.getType() != ArticleJson.Type.ITEM) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Article must be of type Item.");
		}
		
	}

}
