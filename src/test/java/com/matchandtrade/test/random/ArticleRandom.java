package com.matchandtrade.test.random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;

@Component
public class ArticleRandom {
	
	@Autowired
	private ArticleRepositoryFacade articleRepository;

	public static ArticleEntity nextEntity() {
		return ArticleTransformer.transform(nextJson());
	}
	
	public static ArticleJson nextJson() {
		return new ArticleJson();
	}
	
	@Transactional
	public ArticleEntity nextPersistedEntity() {
		ArticleEntity result = nextEntity();
		articleRepository.save(result);
		return result;
	}

	@Transactional
	public ArticleEntity nextPersistedEntity(String name) {
		ArticleEntity result = new ArticleEntity();
		articleRepository.save(result);
		return result;
	}
	
}