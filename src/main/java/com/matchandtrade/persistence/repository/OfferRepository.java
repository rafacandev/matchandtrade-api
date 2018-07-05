package com.matchandtrade.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.matchandtrade.persistence.entity.OfferEntity;

public interface OfferRepository extends CrudRepository<OfferEntity, Integer>{

	List<OfferEntity> findByOfferedItemArticleId(Integer offeredArticleId);
	
}
