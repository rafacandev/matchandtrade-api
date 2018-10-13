package com.matchandtrade.persistence.facade;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.matchandtrade.rest.v1.json.ArticleJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.repository.ArticleRepository;

@Repository
public class ArticleRepositoryFacade {
	
	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private EntityManager entityManger;

	public ArticleEntity get(Integer articleId) {
		return articleRepository.findOne(articleId);
	}

	/**
	 * True if all articleIds belong to an existing {@code Article} 
	 * @param articleIds
	 */
	public boolean exists(Integer[] articleIds) {
		List<Integer> ids = Arrays.asList(articleIds);
		TypedQuery<Integer> query = entityManger.createQuery("SELECT a.articleId FROM ArticleEntity AS a WHERE a.articleId IN (:ids)", Integer.class);
		query.setParameter("ids", ids);
		query.setMaxResults(articleIds.length);
		List<Integer> resultList = query.getResultList();
		return (resultList.size() == articleIds.length);
	}
	
	public void save(ArticleEntity entity) {
		articleRepository.save(entity);
	}

	public void delete(Integer articleId) {
		articleRepository.delete(articleId);
	}

	public ArticleEntity getByUserIdAndArticleId(Integer userId, Integer articleId) {
		return articleRepository.findArticleByUserIdAndArticleId(userId, articleId);
	}
}
