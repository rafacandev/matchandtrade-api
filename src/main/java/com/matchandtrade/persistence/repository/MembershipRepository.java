package com.matchandtrade.persistence.repository;

import com.matchandtrade.persistence.entity.MembershipEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MembershipRepository extends CrudRepository<MembershipEntity, Integer> {
	List<MembershipEntity> findByTrade_TradeId(Integer tradeId);

	MembershipEntity findByOffers_OfferId(Integer offerId);

	Page<MembershipEntity> findByArticles_ArticleId(Integer articleId, Pageable pageable);
}
