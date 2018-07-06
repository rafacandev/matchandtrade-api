package com.matchandtrade.rest.v1.transformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.repository.ItemRepository;
import com.matchandtrade.rest.v1.json.OfferJson;

@Component
public class OfferTransformer {
	
	@Autowired
	private ItemRepository itemRepository;
	
	// Utility classes should not have public constructors
	private OfferTransformer() {}

	public OfferEntity transform(OfferJson json) {
		OfferEntity result = new OfferEntity();
		result.setOfferId(json.getOfferId());
		result.setOfferedArticle(itemRepository.findOne(json.getOfferedArticleId()));
		result.setWantedArticle(itemRepository.findOne(json.getWantedArticleId()));
		return result;
	}

	public static OfferJson transform(OfferEntity entity) {
		OfferJson result = new OfferJson();
		result.setOfferId(entity.getOfferId());
		result.setOfferedArticleId(entity.getOfferedArticle().getArticleId());
		result.setWantedArticleId(entity.getWantedArticle().getArticleId());
		return result;
	}
	
	public static SearchResult<OfferJson> transform(SearchResult<OfferEntity> searchResult) {
		List<OfferJson> resultList = new ArrayList<>();
		for (OfferEntity e : searchResult.getResultList()) {
			resultList.add(transform(e));
		}
		return new SearchResult<>(resultList, searchResult.getPagination());
	}

}
