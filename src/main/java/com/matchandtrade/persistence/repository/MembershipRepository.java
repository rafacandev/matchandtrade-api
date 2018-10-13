package com.matchandtrade.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.MembershipEntity;

@Repository
public interface MembershipRepository extends CrudRepository<MembershipEntity, Integer> {

	List<MembershipEntity> findByTrade_TradeId(Integer tradeId);

	MembershipEntity findByOffers_OfferId(Integer offerId);

}