package com.matchandtrade.rest.v1.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.Authorization;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.Controller;
import com.matchandtrade.rest.transformer.UserTransformer;
import com.matchandtrade.rest.v1.json.UserJson;

//@Api(value = "/users")
//@Path("/users")
@RestController
@RequestMapping(path="/rest/v1/users")
public class UserController extends Controller {

	@Autowired
	private Authorization authorization;
	@Autowired
	private UserModel model;
	
//	@GET
//	@Produces("application/json")
//	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	@Path("/{userId}")
	@RequestMapping(path="/{userId}", method=RequestMethod.GET)
	public UserJson get(@PathVariable("userId") Integer userId) {
		// Check authorization for this operation
		authorization.validateIdentityAndDoBasicAuthorization(getUserAuthentication(), userId);
		// Delegate to model layer
		UserEntity userEntity = model.get(userId);
		// Transform the response
		UserJson result = UserTransformer.transform(userEntity);
		return result;
	}


//	@Autowired
//	private Authorization authorization;
//	private SessionProvider sessionProvider;
//	@Autowired
//	private TradeListModel tradeListModel;
//	@Autowired
//	private UserModel model;
//	@Autowired
//	private UserValidator userValidator;
//	private UserTransformer userTransformer = new UserTransformer();
//	private TradeListTransformer tradeListTransformer = new TradeListTransformer();
//
//	@GET
//	@Produces("application/json")
//	@Path("/{userId}")
//	public UserJson get(@PathParam("userId") Integer userId) {
//		// Check authorization for this operation
//		authorization.validateIdentityAndDoBasicAuthorization(sessionProvider.getUserAuthentication(), userId);
//		// Delegate to model layer
//		UserEntity userEntity = model.get(userId);
//		// Transform the response
//		UserJson result = userTransformer.transform(userEntity);
//		return result;
//	}
//
//    @GET
//    @Produces("application/json")
//    @Path("/{userId}/tradelists")
//    public SearchResult<TradeListJson> getTradeLists(
//    		@PathParam("userId") Integer userId,
//    		@QueryParam("_page") Integer _page,
//    		@QueryParam("_limit") Integer _limit) {
//    	// Check authorization for this operation
//    	authorization.doBasicAuthorization(sessionProvider.getUserAuthentication());
//    	authorization.validateIdentityAndDoBasicAuthorization(sessionProvider.getUserAuthentication(), userId);
//		// Validate the request
//		userValidator.validatePagination(_page, _limit);
//    	// Transform the request
//    	SearchCriteria sc = new SearchCriteria(new Pagination(_page, _limit));
//    	sc.addCriterion(UserEntity.Field.userId, sessionProvider.getUserAuthentication().getUserId());
//    	// Delegate to model
//    	SearchResult<TradeListEntity> searchResultEntity = tradeListModel.search(sc);
//    	// Transform the result
//    	List<TradeListJson> resultList = new JsonArrayList<TradeListJson>();
//    	for (TradeListEntity e : searchResultEntity.getResultList()) {
//    		TradeListJson j = tradeListTransformer.transform(e);
//    		resultList.add(j);
//		}
//    	SearchResult<TradeListJson> searchResultJson = new SearchResult<TradeListJson>(resultList, searchResultEntity.getPagination());
//        return searchResultJson;
//    }
//	
//	@PUT
//    @Produces("application/json")
//    @Consumes("application/json")
//    @Path("/{userId}")
//    public UserJson put(@PathParam("userId") Integer userId, UserJson requestJson) {
//		// Check authorization for this operation
//		authorization.validateIdentityAndDoBasicAuthorization(sessionProvider.getUserAuthentication(), userId);
//    	// Validate the request
//		userValidator.validatePut(userId, requestJson);
//		// Transform the request
//		requestJson.setUserId(userId);
//		UserEntity requestEntity = model.get(userId);
//		userTransformer.transform(requestJson, requestEntity);
//    	// Delegate to model layer
//    	UserEntity resultEntity = model.save(requestEntity);
//    	// Transform the response
//    	UserJson result = userTransformer.transform(resultEntity);
//        return result;
//    }
//	
//	@POST
//    @Produces("application/json")
//    @Consumes("application/json")
//    @Path("{userId}/tradelists")
//    public TradeListJson postTradeLists(@PathParam("userId")Integer userId, TradeListJson requestJson) {
//		// Check authorization for this operation
//		authorization.validateIdentityAndDoBasicAuthorization(sessionProvider.getUserAuthentication(), userId);
//		// Transform the request
//		TradeListEntity tradeListRequestEntity = tradeListTransformer.transform(requestJson);
//    	// Delegate to model layer
//		model.saveTradeList(userId, tradeListRequestEntity);
//    	// Transform the response
//    	TradeListJson result = tradeListTransformer.transform(tradeListRequestEntity);
//        return result;
//    }
//	
//	/**
//	 * Used for dependency injection by the web container using JAX-RS
//	 */
//	@Context
//	public void setContext(MessageContext context){
//		sessionProvider = new SessionProvider(context);
//	}
//	
//	public void setSessionProvider(SessionProvider sessionProvider) {
//		this.sessionProvider = sessionProvider;
//	}
}
