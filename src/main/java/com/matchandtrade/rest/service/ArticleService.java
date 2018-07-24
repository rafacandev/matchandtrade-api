package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;

@Service
public class ArticleService {

	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;

	@Transactional
	public void create(ArticleEntity entity) {
		articleRepositoryFacade.save(entity);
	}

}
