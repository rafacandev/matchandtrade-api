package com.matchandtrade.persistence.facade;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.common.PersistenceUtil;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.FileEntity;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.repository.ItemRepository;

@Repository
public class ItemRepositoryFacade {
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private EntityManager entityManger;

	public ItemEntity get(Integer itemId) {
		return itemRepository.findOne(itemId);
	}

	/**
	 * True if all itemIds belong to an existing {@code Item} 
	 * @param itemIds
	 */
	public boolean exists(Integer[] itemIds) {
		List<Integer> ids = Arrays.asList(itemIds);
		TypedQuery<Integer> query = entityManger.createQuery("SELECT item.itemId FROM ItemEntity AS item WHERE item.itemId IN (:ids)", Integer.class);
		query.setParameter("ids", ids);
		query.setMaxResults(itemIds.length);
		List<Integer> resultList = query.getResultList();
		return (resultList.size() == itemIds.length);
	}
	
	public void save(ItemEntity entity) {
		itemRepository.save(entity);
	}

	public void delete(Integer itemId) {
		itemRepository.delete(itemId);
	}

	public SearchResult<FileEntity> findFilesByItemId(Integer itemId, Integer pageNumber, Integer pageSize) {
		Pageable pageable = PersistenceUtil.buildPageable(pageNumber, pageSize);
		Page<FileEntity> page = itemRepository.findFilesByItemId(itemId, pageable);
		return PersistenceUtil.buildSearchResult(pageable, page);
	}
	
}
