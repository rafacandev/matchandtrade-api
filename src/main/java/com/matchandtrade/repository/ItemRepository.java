package com.matchandtrade.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.dao.ItemDao;
import com.matchandtrade.persistence.entity.ItemEntity;

@Repository
public class ItemRepository {

	@Autowired
	private ItemDao itemDao;

	@Transactional
	public ItemEntity get(Integer itemId) {
		return itemDao.get(ItemEntity.class, itemId);
	}
	
	@Transactional
	public void save(ItemEntity entity) {
		itemDao.save(entity);
	}


}
