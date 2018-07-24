package com.matchandtrade.rest.v1.validator;

import org.springframework.stereotype.Component;

import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.json.ItemJson;

@Component
public class ArticleValidator {

	
	public void validatePost(Integer userId, ArticleJson json) {
		if (json instanceof ItemJson) {
			//TODO
		}
	}

}
