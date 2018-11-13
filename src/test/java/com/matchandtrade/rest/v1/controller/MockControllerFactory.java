package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.AuthenticationRespositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.test.random.UserRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <b>Warning</b> do not use <code>@Autowired *Controller</code> in your tests.
 * Controller classes uses ScopeRequest; so the container will creates an instance per request.
 * Integration tests do run in a container hence treat each controller as singleton.
 * 
 * Treating controllers as singleton may cause testing leakage because they need to
 * access the HttpServletRequest to handle authentication.
 * 
 * As a result, it is required to create a new instance of the {@code *Controller}
 * for every test and configure the dependencies manually,
 * this can be achieved through {@code MockControllerFactory.get*Controller()} 
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
@Component
public class MockControllerFactory {

	@Autowired
	private AuthenticationRespositoryFacade authentRepository;
	@Autowired
	private AuthenticationController authenticationController;
	@Autowired
	private ArticleController articleController;
	private MockAuthenticationProvider lastMockAuthenticationProvider;
	@Autowired
	private OfferController offerController;
	@Autowired
	private SearchController searchController;
	@Autowired
	private TradeController tradeController;
	@Autowired
	private TradeResultController tradeResultController;
	@Autowired
	private MembershipController membershipController;
	@Autowired
	private UserController userController;
	@Autowired
	private UserRepositoryFacade userRepository;
	@Autowired
	private ArticleAttachmentController articleFileController;

	@Autowired
	private ListingController listingController;


	private class MockAuthenticationProvider extends AuthenticationProvider {
		public AuthenticationEntity authenticationEntity;
		public MockAuthenticationProvider() {
			UserEntity authenticatedUserEntity = UserRandom.createEntity();
			userRepository.save(authenticatedUserEntity);
			AuthenticationEntity authenticationEntity = new AuthenticationEntity();
			authenticationEntity.setToken("MockControllerFactory#userId: " + authenticatedUserEntity.getUserId());
			authenticationEntity.setUser(authenticatedUserEntity);
			authentRepository.save(authenticationEntity);
			this.authenticationEntity = authenticationEntity;
		}
		@Override
		public AuthenticationEntity getAuthentication() {
			return this.authenticationEntity;
		}
	}
	
	public AuthenticationController getAuthenticationController() {
		authenticationController.authenticationProvider = new MockAuthenticationProvider();
		return authenticationController;
	}
	
	public ArticleController getArticleController() {
		articleController.authenticationProvider = new MockAuthenticationProvider();
		return articleController;
	}

	public TradeController getTradeController() {
		TradeController result = new TradeController();
		result.tradeService = tradeController.tradeService;
		result.tradeValidador = tradeController.tradeValidador;
		result.authenticationProvider = new MockAuthenticationProvider();
		return result;
	}

	public TradeResultController getTradeResultController() {
		tradeResultController.authenticationProvider = new MockAuthenticationProvider();
		return tradeResultController;
	}

	public ListingController getListingController() {
		listingController.authenticationProvider = new MockAuthenticationProvider();
		return listingController;
	}

	public MembershipController getMembershipController() {
		membershipController.authenticationProvider = new MockAuthenticationProvider();
		return membershipController;
	}
	
	public UserController getUserController() {
		userController.authenticationProvider = new MockAuthenticationProvider();
		return userController;
	}

	public SearchController getSearchController() {
		searchController.authenticationProvider = new MockAuthenticationProvider();
		return searchController;
	}

	public OfferController getOfferController() {
		OfferController result = new OfferController();
		result.authenticationProvider = new MockAuthenticationProvider();
		result.offerService = offerController.offerService;
		result.offerTransformer = offerController.offerTransformer;
		result.offerValidator = offerController.offerValidator;
		return result;
	}

	public ArticleAttachmentController getArticleFileController() {
		articleFileController.authenticationProvider = new MockAuthenticationProvider();
		return articleFileController;
	}

}
