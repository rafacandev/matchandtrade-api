package com.matchandtrade.rest.v1.transformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.rest.v1.json.ItemJson;

@Component
public class ItemTransformer {

	public static ItemEntity transform(ItemJson json) {
		ItemEntity result = new ItemEntity();
		result.setName(json.getName());
		result.setItemId(json.getItemId());
		return result;
	}

	public static ItemJson transform(ItemEntity itemEntity) {
		ItemJson result = new ItemJson();
		result.setName(itemEntity.getName());
		result.setItemId(itemEntity.getItemId());
		return result;
	}

	public static SearchResult<ItemJson> transform(SearchResult<ItemEntity> searchResult) {
		List<ItemJson> resultList = new ArrayList<ItemJson>();
		for (ItemEntity e : searchResult.getResultList()) {
			resultList.add(transform(e));
		}
		return new SearchResult<>(resultList, searchResult.getPagination());
	}

}
