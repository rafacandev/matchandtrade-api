package com.matchandtrade.persistence.repository;

import com.matchandtrade.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<UserEntity, Integer>{
	UserEntity findByEmail(String email);

	@Query("FROM UserEntity AS user" +
		" INNER JOIN user.articles AS article" +
		" WHERE article.articleId = :articleId")
	UserEntity findByArticleId(@Param("articleId") Integer articleId);

	@Query("SELECT u "
			+ " FROM MembershipEntity membership"
			+ " INNER JOIN membership.user AS u"
			+ " INNER JOIN membership.offers AS o"
			+ " WHERE"
			+ " o.offerId = :offerId")
	UserEntity findByOfferId(@Param("offerId")Integer offerId);
}
