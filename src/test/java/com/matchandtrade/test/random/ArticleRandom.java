package com.matchandtrade.test.random;


import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import com.matchandtrade.persistence.repository.UserRepository;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ArticleRandom {
	
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private AttachmentRepositoryFacade attachmentRepositoryFacade;
	private static ArticleTransformer articleTransformer = new ArticleTransformer();
	@Autowired
	private MembershipRepositoryFacade membershipRepository;
	@Autowired
	private MembershipRandom membershipRandom;
	@Autowired
	private UserRepository userRepository;
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
		userRepository.save(user);
		return result;
	}

	@Transactional
	public ArticleEntity createPersistedEntity(MembershipEntity membership) {
		MembershipEntity persistedMembership = membershipRepository.find(membership.getMembershipId());
		ArticleEntity result = createEntity();
		persistArticleAndMembershipAndUser(persistedMembership, result);
		return result;
	}

	@Transactional
	public ArticleEntity createPersistedEntity(MembershipEntity membership, String articleName) {
		MembershipEntity persistedMembership = membershipRepository.find(membership.getMembershipId());
		ArticleEntity result = createEntity();
		result.setName(articleName);
		persistArticleAndMembershipAndUser(persistedMembership, result);
		return result;
	}

	@Transactional
	public ArticleEntity createPersistedEntity(UserEntity user) {
		ArticleEntity result = createEntity();
		articleRepositoryFacade.save(result);
		UserEntity persistedUser = userRepository.findOne(user.getUserId());
		persistedUser.getArticles().add(result);
		userRepository.save(persistedUser);
		return result;
	}

	private void persistArticleAndMembershipAndUser(MembershipEntity persistedMembership, ArticleEntity result) {
		articleRepositoryFacade.save(result);
		persistedMembership.getArticles().add(result);
		membershipRepository.save(persistedMembership);
		persistedMembership.getUser().getArticles().add(result);
		userRepository.save(persistedMembership.getUser());
	}

}