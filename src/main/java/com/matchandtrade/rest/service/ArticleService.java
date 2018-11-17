package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleService {

	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private UserRepositoryFacade userRepositoryFacade;

	@Transactional
	public void create(UserEntity user, ArticleEntity article) {
		articleRepositoryFacade.save(article);
		user.getArticles().add(article);
		userRepositoryFacade.save(user);
	}

	public ArticleEntity find(Integer articleId) {
		return articleRepositoryFacade.get(articleId);
	}

	public void update(ArticleEntity article) {
		articleRepositoryFacade.save(article);
	}

	@Transactional
	public void delete(Integer articleId) {
		UserEntity user = userRepositoryFacade.findByArticleId(articleId);
		ArticleEntity article = articleRepositoryFacade.get(articleId);
		user.getArticles().remove(article);
		userRepositoryFacade.save(user);
		articleRepositoryFacade.delete(articleId);
	}

	/**
	 * Returns true when all {@articleId}s are associated with existing articles
	 *
	 * @param articleIds
	 * @return
	 */
	public boolean exists(Integer... articleIds) {
		return articleRepositoryFacade.exists(articleIds);
	}

	public SearchResult<ArticleEntity> search(Integer pageNumber, Integer pageSize) {
		return articleRepositoryFacade.findAll(pageNumber, pageSize);
	}

}
