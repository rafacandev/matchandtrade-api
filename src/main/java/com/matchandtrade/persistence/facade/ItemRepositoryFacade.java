package com.matchandtrade.persistence.facade;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.repository.ItemRepository;

@Repository
public class ItemRepositoryFacade {
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private EntityManager entityManger;

	public ItemEntity get(Integer articleId) {
		return itemRepository.findOne(articleId);
	}

	/**
	 * True if all articleIds belong to an existing {@code Item} 
	 * @param articleIds
	 */
	public boolean exists(Integer[] articleIds) {
		List<Integer> ids = Arrays.asList(articleIds);
		TypedQuery<Integer> query = entityManger.createQuery("SELECT item.articleId FROM ItemEntity AS item WHERE item.articleId IN (:ids)", Integer.class);
		query.setParameter("ids", ids);
		query.setMaxResults(articleIds.length);
		List<Integer> resultList = query.getResultList();
		return (resultList.size() == articleIds.length);
	}
	
	public void save(ItemEntity entity) {
		itemRepository.save(entity);
	}

	public void delete(Integer articleId) {
		itemRepository.delete(articleId);
	}
	
}
