package com.matchandtrade.persistence.facade;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.repository.OfferRepository;

@Repository
public class OfferRepositoryFacade {
	
	@Autowired
	private OfferRepository offerRepository;
	
	@Autowired
	private EntityManager entityManager;

	/**
	 * True if all items are associated to the same {@code Trade}.
	 * @param itemIds: {@code Item.itemId} of all {@Items} to verify.
	 */
	public boolean areItemsAssociatedToSameTrade(Integer[] itemIds) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT trade FROM TradeMembershipEntity AS tradeMembership");
		hql.append(" INNER JOIN tradeMembership.trade AS trade");
		hql.append(" INNER JOIN tradeMembership.items AS item");
		hql.append(" WHERE");
		hql.append(" item.itemId IN (:ids)");
		hql.append(" GROUP BY trade");
		List<Integer> ids = Arrays.asList(itemIds);
		TypedQuery<TradeEntity> query = entityManager.createQuery(hql.toString(), TradeEntity.class);
		query.setParameter("ids", ids);
		query.setMaxResults(2);
		List<TradeEntity> resultList = query.getResultList();
		return (resultList.size() == 1);
	}
	
	public void save(OfferEntity entity) {
		offerRepository.save(entity);
	}

	public OfferEntity get(Integer offerId) {
		return offerRepository.findOne(offerId);
	}

	public List<OfferEntity> getByOfferedItemId(Integer offeredItemId) {
		return offerRepository.findByOfferedItemItemId(offeredItemId);
	}
}
