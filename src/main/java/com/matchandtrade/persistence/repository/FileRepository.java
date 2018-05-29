package com.matchandtrade.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.matchandtrade.persistence.entity.FileEntity;

public interface FileRepository extends CrudRepository<FileEntity, Integer>{

	@Query( value = "SELECT i.files FROM ItemEntity i WHERE i.itemId = :itemId",
			countQuery = "SELECT COUNT(*) FROM ItemEntity i INNER JOIN i.files AS file WHERE i.itemId = :itemId")
	Page<FileEntity> findFilesByItemId(@Param("itemId")Integer itemId, Pageable pageable);

}
