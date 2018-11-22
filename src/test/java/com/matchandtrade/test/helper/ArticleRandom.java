package com.matchandtrade.test.helper;


import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ArticleRandom {
	
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	private static ArticleTransformer articleTransformer = new ArticleTransformer();
	@Autowired
	private MembershipRepositoryFacade membershipRepositoryFacade;
	@Autowired
	private MembershipRandom membershipRandom;
	@Autowired
	private UserRepositoryFacade userRepositoryFacade;
	@Autowired
	private UserRandom userRandom;

	public static ArticleEntity createEntity() {
		return articleTransformer.transform(createJson());
	}

	public static ArticleJson createJson() {
		ArticleJson result = new ArticleJson();
		result.setName(StringRandom.nextName());
		return result;
	}

	@Transactional
	public ArticleEntity createPersistedEntity() {
		ArticleEntity result = createEntity();
		articleRepositoryFacade.save(result);
		UserEntity user = userRandom.createPersistedEntity();
		user.getArticles().add(result);
		userRepositoryFacade.save(user);
		return result;
	}

	@Transactional
	public ArticleEntity createPersistedEntity(MembershipEntity membership) {
		MembershipEntity persistedMembership = membershipRepositoryFacade.find(membership.getMembershipId());
		ArticleEntity result = createEntity();
		persistArticleAndMembershipAndUser(persistedMembership, result);
		return result;
	}

	@Transactional
	public ArticleEntity createPersistedEntity(MembershipEntity membership, String articleName) {
		MembershipEntity persistedMembership = membershipRepositoryFacade.find(membership.getMembershipId());
		ArticleEntity result = createEntity();
		result.setName(articleName);
		persistArticleAndMembershipAndUser(persistedMembership, result);
		return result;
	}

	@Transactional
	public ArticleEntity createPersistedEntity(UserEntity user) {
		ArticleEntity result = createEntity();
		articleRepositoryFacade.save(result);
		UserEntity persistedUser = userRepositoryFacade.find(user.getUserId());
		persistedUser.getArticles().add(result);
		userRepositoryFacade.save(persistedUser);
		return result;
	}

	private void persistArticleAndMembershipAndUser(MembershipEntity persistedMembership, ArticleEntity result) {
		articleRepositoryFacade.save(result);
		persistedMembership.getArticles().add(result);
		membershipRepositoryFacade.save(persistedMembership);
		persistedMembership.getUser().getArticles().add(result);
		userRepositoryFacade.save(persistedMembership.getUser());
	}

}