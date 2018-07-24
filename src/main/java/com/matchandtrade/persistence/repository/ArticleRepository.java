package com.matchandtrade.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.matchandtrade.persistence.entity.ArticleEntity;

public interface ArticleRepository extends CrudRepository<ArticleEntity, Integer>{

}
