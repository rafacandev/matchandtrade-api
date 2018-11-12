package com.matchandtrade.test.random;


import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import com.matchandtrade.persistence.repository.UserRepository;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ArticleRandom {
	
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private AttachmentRepositoryFacade attachmentRepositoryFacade;
	@Autowired
	private MembershipRepositoryFacade membershipRepository;
	@Autowired
	private MembershipRandom membershipRandom;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRandom userRandom;
	

	public static ArticleEntity nextEntity() {
		return ArticleTransformer.transform(createJson());
	}
	
	public static ArticleJson createJson() {
		ArticleJson result = new ArticleJson();
		result.setName(StringRandom.nextName());
		return result;
	}
	
	@Transactional
	public ArticleEntity createPersistedEntity(MembershipEntity membership) {
		MembershipEntity tme = membershipRepository.get(membership.getMembershipId());
		ArticleEntity result = nextEntity();
		articleRepositoryFacade.save(result);
		tme.getArticles().add(result);
		membershipRepository.save(tme);
		tme.getUser().getArticles().add(result);
		userRepository.save(tme.getUser());
		return result;
	}

	@Transactional
	public ArticleEntity createPersistedEntity(MembershipEntity membership, String name) {
		MembershipEntity tme = membershipRepository.get(membership.getMembershipId());
		ArticleEntity result = new ArticleEntity();
		result.setName(name);
		articleRepositoryFacade.save(result);
		tme.getArticles().add(result);
		membershipRepository.save(tme);
		return result;
	}
	
	@Transactional
	public ArticleEntity createPersistedEntity(UserEntity tradeOwner) {
		MembershipEntity existingTradeMemberhip = membershipRandom.createPersistedEntity(tradeOwner);
		return createPersistedEntity(existingTradeMemberhip);
	}

	@Transactional
	public ArticleEntity createPersistedEntity(UserEntity user, boolean shouldCreateTrade) {
		if (shouldCreateTrade == true) {
			return createPersistedEntity(user);
		}
		user = userRepository.findOne(user.getUserId());
		ArticleEntity result = nextEntity();
		articleRepositoryFacade.save(result);
		user.getArticles().add(result);
		userRepository.save(user);
		return result;
	}

	@Transactional
	public ArticleEntity createPersistedEntity() {
		ArticleEntity result = nextEntity();
		articleRepositoryFacade.save(result);
		UserEntity user = userRandom.createPersistedEntity();
		user.getArticles().add(result);
		userRepository.save(user);
		return result;
	}

	@Transactional
	public ArticleEntity createAttachmentToArticle(ArticleEntity article, String attachmentName) {
		ArticleEntity persistedArticle = articleRepositoryFacade.get(article.getArticleId());
		AttachmentEntity attachment = new AttachmentEntity();
		attachment.setName(attachmentName);
		attachmentRepositoryFacade.save(attachment);
		persistedArticle.getAttachments().add(attachment);
		articleRepositoryFacade.save(persistedArticle);
		return persistedArticle;
	}

}