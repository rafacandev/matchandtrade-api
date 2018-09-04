package com.matchandtrade.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.matchandtrade.persistence.entity.AttachmentEntity;

public interface AttachmentRepository extends CrudRepository<AttachmentEntity, Integer>{

	@Query( value = "SELECT i.attachments FROM ArticleEntity i WHERE i.articleId = :articleId",
			countQuery = "SELECT COUNT(*) FROM ArticleEntity i INNER JOIN i.attachments AS file WHERE i.articleId = :articleId")
	Page<AttachmentEntity> findAttachmentsByArticleId(@Param("articleId")Integer articleId, Pageable pageable);

}
