package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.entity.WantItemEntity;
import com.matchandtrade.rest.v1.json.WantItemJson;

public class WantItemTransformer {
	
	// Utility classes should not have public constructors
	private WantItemTransformer() {}

	public static WantItemEntity transform(WantItemJson json) {
		WantItemEntity result = new WantItemEntity();
		result.setItem(ItemTransformer.transform(json.getItem()));
		result.setPriority(json.getPriority());
		result.setWantItemId(json.getWantItemId());
		return result;
	}

	public static WantItemJson transform(WantItemEntity entity) {
		WantItemJson result = new WantItemJson();
		result.setItem(ItemTransformer.transform(entity.getItem()));
		result.setPriority(entity.getPriority());
		result.setWantItemId(entity.getWantItemId());
		return result;
	}

}
