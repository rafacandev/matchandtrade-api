package com.matchandtrade.rest.v1.json.search;


import java.util.ArrayList;
import java.util.List;

public class SearchCriteriaJson {
	
	private Recipe recipe;
	
	private List<Criterion> criteria = new ArrayList<>();

	public void addCriterion(String key, Object value) {
		criteria.add(new Criterion(key, value));
	}

	public void addCriterion(String key, Object value, Operator operator, Matcher restriction) {
		criteria.add(new Criterion(key, value, operator, restriction));
	}

	public List<Criterion> getCriteria() {
		return criteria;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setCriteria(List<Criterion> criteria) {
		this.criteria = criteria;
	}
	
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
	
}
