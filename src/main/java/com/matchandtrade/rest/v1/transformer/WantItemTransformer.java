package com.matchandtrade.rest.v1.transformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.WantItemEntity;
import com.matchandtrade.persistence.facade.ItemRepositoryFacade;
import com.matchandtrade.rest.v1.json.WantItemJson;

@Component
public class WantItemTransformer {
	
	@Autowired
	private ItemRepositoryFacade itemRepositoryFacade;
	
	// Utility classes should not have public constructors
	private WantItemTransformer() {}

	public WantItemEntity transform(WantItemJson json) {
		WantItemEntity result = new WantItemEntity();
		result.setItem(itemRepositoryFacade.get(json.getItemId()));
		result.setPriority(json.getPriority());
		result.setWantItemId(json.getWantItemId());
		return result;
	}

	public static WantItemJson transform(WantItemEntity entity) {
		if (entity == null) {
			return null;
		}
		WantItemJson result = new WantItemJson();
		if (entity.getItem() != null) {
			result.setItemId(entity.getItem().getItemId());
		}
		result.setPriority(entity.getPriority());
		result.setWantItemId(entity.getWantItemId());
		return result;
	}

	public static SearchResult<WantItemJson> transform(SearchResult<WantItemEntity> searchResult) {
		List<WantItemJson> resultList = new ArrayList<>();
		for(WantItemEntity e : searchResult.getResultList()) {
			resultList.add(transform(e));
		}
		return new SearchResult<>(resultList, searchResult.getPagination());
	}

}
