package com.matchandtrade.persistence.facade;

import com.matchandtrade.persistence.common.PersistenceUtil;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepositoryFacade {
	@Autowired
	private ArticleRepository articleRepository;

	// TODO: Optional?
	public ArticleEntity findByArticleId(Integer articleId) {
		return articleRepository.findById(articleId).get();
	}

	public void save(ArticleEntity entity) {
		articleRepository.save(entity);
	}

	public void delete(Integer articleId) {
		articleRepository.deleteById(articleId);
	}

	public ArticleEntity findByUserIdAndArticleId(Integer userId, Integer articleId) {
		return articleRepository.findArticleByUserIdAndArticleId(userId, articleId);
	}

	public SearchResult<ArticleEntity> findAll(Integer pageNumber, Integer pageSize) {
		Pageable pageable = PersistenceUtil.buildPageable(pageNumber, pageSize);
		Page<ArticleEntity> page = articleRepository.findAll(pageable);
		return PersistenceUtil.buildSearchResult(pageable, page);
	}
}
