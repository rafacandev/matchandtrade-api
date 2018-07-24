package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.repository.ArticleRepository;

@Repository
public class ArticleRepositoryFacade {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	public void save(ArticleEntity entity) {
		articleRepository.save(entity);
	}
	
}
