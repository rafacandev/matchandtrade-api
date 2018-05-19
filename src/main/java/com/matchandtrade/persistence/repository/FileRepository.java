package com.matchandtrade.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.matchandtrade.persistence.entity.FileEntity;

public interface FileRepository extends CrudRepository<FileEntity, Integer>{

}
