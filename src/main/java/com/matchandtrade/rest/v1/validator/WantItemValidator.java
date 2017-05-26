package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.criteria.WantItemQueryBuilder;
import com.matchandtrade.persistence.entity.WantItemEntity;
import com.matchandtrade.repository.WantItemRepository;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.WantItemJson;

@Component
public class WantItemValidator {

	@Autowired
	private WantItemRepository wantItemRepository;
	
	public void validatePost(Integer tradeMembershipId, Integer itemId, WantItemJson request) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(1,1));
		searchCriteria.addCriterion(WantItemQueryBuilder.Criterion.tradeMembershipId, tradeMembershipId);
		searchCriteria.addCriterion(WantItemQueryBuilder.Criterion.itemId, itemId);
		searchCriteria.addCriterion(WantItemQueryBuilder.Criterion.priority, request.getPriority());
		SearchResult<WantItemEntity> searchResult = wantItemRepository.query(searchCriteria);
		if (!searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "WantItem.priority must be unique within the same Item.");
		}
	}

}
