package com.matchandtrade.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.matchandtrade.persistence.entity.AttachmentEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface AttachmentRepository extends CrudRepository<AttachmentEntity, UUID>{
	@Query( value = "SELECT i.attachments FROM ArticleEntity i WHERE i.articleId = :articleId",
			countQuery = "SELECT COUNT(*) FROM ArticleEntity i INNER JOIN i.attachments AS file WHERE i.articleId = :articleId")
	Page<AttachmentEntity> findAttachmentsByArticleId(@Param("articleId")Integer articleId, Pageable pageable);

	@Transactional
	@Modifying
	@Query(nativeQuery = true,
		value = "DELETE FROM article_to_attachment WHERE article_to_attachment.attachment_id = :attachmentId")
	void deleteArticleAttachmentAssociation(@Param("attachmentId") UUID attachmentId);
}
