package com.matchandtrade.persistence.repository;

import com.matchandtrade.persistence.entity.OfferEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface OfferRepository extends CrudRepository<OfferEntity, Integer>{
	Page<OfferEntity> findByOfferedArticleArticleId(Integer offeredArticleId, Pageable pageable);
}
