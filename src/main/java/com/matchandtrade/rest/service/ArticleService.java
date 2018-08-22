package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;

@Service
public class ArticleService {

	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;

	public void create(ArticleEntity entity) {
		articleRepositoryFacade.save(entity);
	}

	public ArticleEntity get(Integer articleId) {
		return articleRepositoryFacade.get(articleId);
	}

	public void update(ArticleEntity entity) {
		articleRepositoryFacade.save(entity);
	}

}
