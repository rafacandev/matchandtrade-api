package com.matchandtrade.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.matchandtrade.persistence.entity.AttachmentEntity;

public interface AttachmentRepository extends CrudRepository<AttachmentEntity, Integer>{

	@Query( value = "SELECT i.attachments FROM ItemEntity i WHERE i.itemId = :itemId",
			countQuery = "SELECT COUNT(*) FROM ItemEntity i INNER JOIN i.attachments AS file WHERE i.itemId = :itemId")
	Page<AttachmentEntity> findAttachmentsByItemId(@Param("itemId")Integer itemId, Pageable pageable);

}
