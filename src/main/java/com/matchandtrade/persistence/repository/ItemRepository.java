package com.matchandtrade.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.matchandtrade.persistence.entity.ItemEntity;

public interface ItemRepository extends CrudRepository<ItemEntity, Integer>{

}
