package com.matchandtrade.rest.v1.json.search;

import java.util.ArrayList;
import java.util.List;

public class SearchCriteriaJson {
	private String recipe;
	private List<SortJson> sorts = new ArrayList<>();
	private List<CriterionJson> criteria = new ArrayList<>();

	public void addCriterion(String key, Object value) {
		criteria.add(new CriterionJson(key, value));
	}

	public List<CriterionJson> getCriteria() {
		return criteria;
	}

	public String getRecipe() {
		return recipe;
	}

	public List<SortJson> getSorts() {
		return sorts;
	}

	public void setCriteria(List<CriterionJson> criteria) {
		this.criteria = criteria;
	}
	
	public void setRecipe(String recipe) {
		this.recipe = recipe;
	}

	public void setSorts(List<SortJson> sorts) {
		this.sorts = sorts;
	}
}
