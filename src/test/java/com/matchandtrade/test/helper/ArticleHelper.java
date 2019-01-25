package com.matchandtrade.test.helper;


import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.test.StringRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Commit
public class ArticleHelper {
	
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	private static ArticleTransformer articleTransformer = new ArticleTransformer();
	@Autowired
	private MembershipRepositoryFacade membershipRepositoryFacade;
	@Autowired
	private MembershipHelper membershipHelper;
	@Autowired
	private UserRepositoryFacade userRepositoryFacade;
	@Autowired
	private UserHelper userHelper;

	@Transactional
	public ArticleEntity createPersistedEntity() {
		ArticleEntity result = createRandomEntity();
		articleRepositoryFacade.save(result);
		UserEntity user = userHelper.createPersistedEntity();
		user.getArticles().add(result);
		userRepositoryFacade.save(user);
		return result;
	}

	@Transactional
	public ArticleEntity createPersistedEntity(MembershipEntity membership) {
		MembershipEntity persistedMembership = membershipRepositoryFacade.findByMembershipId(membership.getMembershipId());
		ArticleEntity result = createRandomEntity();
		persistArticleAndMembershipAndUser(persistedMembership, result);
		return result;
	}

	@Transactional
	public ArticleEntity createPersistedEntity(MembershipEntity membership, String articleName) {
		MembershipEntity persistedMembership = membershipRepositoryFacade.findByMembershipId(membership.getMembershipId());
		ArticleEntity result = createRandomEntity();
		result.setName(articleName);
		persistArticleAndMembershipAndUser(persistedMembership, result);
		return result;
	}

	@Transactional
	public ArticleEntity createPersistedEntity(UserEntity user) {
		ArticleEntity result = createRandomEntity();
		articleRepositoryFacade.save(result);
		UserEntity persistedUser = userRepositoryFacade.findByUserId(user.getUserId());
		persistedUser.getArticles().add(result);
		userRepositoryFacade.save(persistedUser);
		return result;
	}

	public static ArticleEntity createRandomEntity() {
		return articleTransformer.transform(createRandomJson());
	}

	public static ArticleJson createRandomJson() {
		ArticleJson result = new ArticleJson();
		result.setName(StringRandom.nextName());
		result.setDescription(StringRandom.nextDescription());
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