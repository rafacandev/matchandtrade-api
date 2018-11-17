package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.Entity;
import com.matchandtrade.rest.Json;

import java.util.ArrayList;
import java.util.List;

public abstract class Transformer<E extends Entity, J extends Json> {

	public abstract J transform(E entity);

	public abstract E transform(J json);

	public SearchResult<J> transform(SearchResult<E> searchResult) {
		List<J> resultList = new ArrayList<>();
		for(E entity : searchResult.getResultList()) {
			resultList.add(transform(entity));
		}
		return new SearchResult<>(resultList, searchResult.getPagination());
	}

}
