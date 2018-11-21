package com.matchandtrade.persistence.facade;

import com.matchandtrade.persistence.common.PersistenceUtil;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class MembershipRepositoryFacade {

	@Autowired
	private MembershipRepository membershipRepository;

	public void delete(Integer membershipId) {
		membershipRepository.delete(membershipId);
	}

	public MembershipEntity find(Integer membershipId) {
		return membershipRepository.findOne(membershipId);
	}

	public MembershipEntity findByOfferId(Integer offerId) {
		return membershipRepository.findByOffers_OfferId(offerId);
	}

	public SearchResult<MembershipEntity> findByArticleIdId(Integer articleId, Integer pageNumber, Integer pageSize) {
		Pageable pageable = PersistenceUtil.buildPageable(pageNumber, pageSize);
		Page<MembershipEntity> page = membershipRepository.findByArticles_ArticleId(articleId, pageable);
		return PersistenceUtil.buildSearchResult(pageable, page);
	}

	public void save(MembershipEntity entity) {
		membershipRepository.save(entity);
	}
	
}
