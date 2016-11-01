package com.matchandtrade.rest.v1.controller;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.Authorization;
import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.Controller;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.rest.v1.transformer.UserTransformer;
import com.matchandtrade.rest.v1.validator.UserValidator;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(path="/rest/v1/users")
public class UserController extends Controller {

	@Autowired
	private Authorization authorization;
	@Autowired
	private UserModel model;
	@Autowired
	private UserValidator userValidador;
	@Autowired
	private UserTransformer userTransformer;

	@ApiResponses(value={@ApiResponse(response=UserJson.class, message="OK", code=200)})
	@RequestMapping(path="/{userId}", method=RequestMethod.GET)
	public UserJson getByUserId(@PathVariable("userId") Integer userId) {
		// Check authorization for this operation
		authorization.validateIdentityAndDoBasicAuthorization(getUserAuthentication(), userId);
		// Delegate to model layer
		UserEntity userEntity = model.get(userId);
		// Transform the response
		UserJson result = UserTransformer.transform(userEntity);
		return result;
	}
	
	@ApiResponses(value={@ApiResponse(response=UserJson.class, responseContainer="List", message="OK", code=200)})
	@RequestMapping(method=RequestMethod.GET)
	public SearchResult<UserJson> searching(
			@RequestParam(required=false) Integer _pageNumber,
			@RequestParam(required=false) Integer _pageSize,
			@RequestParam String email) {
		// Check authorization for this operation
		authorization.doBasicAuthorization(getUserAuthentication());
		// Build SearchCriteria
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		searchCriteria.addCriterion(UserEntity.Field.email, email);
		// Delegate to model layer
		SearchResult<UserEntity> searchResult = model.search(searchCriteria);
		// Transform the response
		SearchResult<UserJson> result = UserTransformer.transform(searchResult);
		return result;
	}

	@Transactional
	@RequestMapping(path="/{userId}", method=RequestMethod.PUT)
	public UserJson put(@PathVariable("userId") Integer userId, @RequestBody UserJson requestJson) {
		// Check authorization for this operation
		authorization.validateIdentityAndDoBasicAuthorization(getUserAuthentication(), userId);
		// Validate the request
		requestJson.setUserId(userId);
		userValidador.validatePut(requestJson);
		// Transform the request
		UserEntity userEntity = userTransformer.transform(requestJson, true);
		// Delegate to model layer
		model.save(userEntity);
		// Transform the response
		UserJson result = UserTransformer.transform(userEntity);
		return result;
	}
}
