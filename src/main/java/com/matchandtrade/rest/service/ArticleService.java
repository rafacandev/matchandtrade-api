package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleService {
@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private UserService userService;

	@Transactional
	public void create(UserEntity user, ArticleEntity article) {
		articleRepositoryFacade.save(article);
		UserEntity persistedUser = userService.findByUserId(user.getUserId());
		persistedUser.getArticles().add(article);
		userService.save(persistedUser);
	}

	public ArticleEntity findByArticleId(Integer articleId) {
		return articleRepositoryFacade.findByArticleId(articleId);
	}

	public SearchResult<ArticleEntity> findAll(Integer pageNumber, Integer pageSize) {
		return articleRepositoryFacade.findAll(pageNumber, pageSize);
	}

	public void update(ArticleEntity article) {
		articleRepositoryFacade.save(article);
	}

	@Transactional
	public void delete(Integer articleId) {
		UserEntity user = userService.findByArticleId(articleId);
		ArticleEntity article = articleRepositoryFacade.findByArticleId(articleId);
		user.getArticles().remove(article);
		userService.save(user);
		articleRepositoryFacade.delete(articleId);
	}

	public ArticleEntity findByUserIdAndArticleId(Integer userId, Integer articleId) {
		return articleRepositoryFacade.findByUserIdAndArticleId(userId, articleId);
	}
}
