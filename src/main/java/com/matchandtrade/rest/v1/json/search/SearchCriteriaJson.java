package com.matchandtrade.rest.v1.json.search;


import java.util.ArrayList;
import java.util.List;

public class SearchCriteriaJson {
	
	private String recipe;
	
	private List<CriterionJson> criteria = new ArrayList<>();

	public void addCriterion(String key, Object value) {
		criteria.add(new CriterionJson(key, value));
	}

	public void addCriterion(String key, Object value, Operator operator, Matcher restriction) {
		criteria.add(new CriterionJson(key, value, operator, restriction));
	}

	public List<CriterionJson> getCriteria() {
		return criteria;
	}

	public String getRecipe() {
		return recipe;
	}

	public void setCriteria(List<CriterionJson> criteria) {
		this.criteria = criteria;
	}
	
	public void setRecipe(String recipe) {
		this.recipe = recipe;
	}
	
}
