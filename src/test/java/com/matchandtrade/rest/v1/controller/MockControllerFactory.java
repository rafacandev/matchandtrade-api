package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.AuthenticationRespositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.test.random.UserRandom;

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
	private ArticleController articleController;
	@Autowired
	private AuthenticationRespositoryFacade authentRepository;
	@Autowired
	private AuthenticationController authenticationController;
	@Autowired
	private ItemController itemController;
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
	private TradeMembershipController tradeMembershipController;
	@Autowired
	private UserController userController;
	@Autowired
	private UserRepositoryFacade userRepository;
	@Autowired
	private ItemAttachmentController itemFileController;
	@Autowired
	private AttachmentController fileController;

	private class MockAuthenticationProvider extends AuthenticationProvider {
		public AuthenticationEntity authenticationEntity;
		public MockAuthenticationProvider() {
			UserEntity authenticatedUserEntity = UserRandom.nextEntity();
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
	
	private MockAuthenticationProvider buildAuthenticationProvider(boolean reusePreviousAuthentication) {
		if (lastMockAuthenticationProvider == null || !reusePreviousAuthentication) {
			lastMockAuthenticationProvider = new MockAuthenticationProvider();
		}
		return lastMockAuthenticationProvider;
	}

	public AuthenticationController getAuthenticationController(boolean reusePreviousAuthentication) {
		authenticationController.authenticationProvider = buildAuthenticationProvider(reusePreviousAuthentication);
		return authenticationController;
	}
	
	public ItemController getItemController(boolean reusePreviousAuthentication) {
		itemController.authenticationProvider = buildAuthenticationProvider(reusePreviousAuthentication);
		return itemController;
	}
	
	public TradeController getTradeController(boolean reusePreviousAuthentication) {
		TradeController result = new TradeController();
		result.tradeService = tradeController.tradeService;
		result.tradeValidador = tradeController.tradeValidador;
		result.authenticationProvider = buildAuthenticationProvider(reusePreviousAuthentication);
		return result;
	}

	public TradeResultController getTradeResultController(boolean reusePreviousAuthentication) {
		tradeResultController.authenticationProvider = buildAuthenticationProvider(reusePreviousAuthentication);
		return tradeResultController;
	}

	public TradeMembershipController getTradeMembershipController(boolean reusePreviousAuthentication) {
		tradeMembershipController.authenticationProvider = buildAuthenticationProvider(reusePreviousAuthentication);
		return tradeMembershipController;
	}
	
	public UserController getUserController(boolean reusePreviousAuthentication) {
		userController.authenticationProvider = buildAuthenticationProvider(reusePreviousAuthentication);
		return userController;
	}

	public SearchController getSearchController(boolean reusePreviousAuthentication) {
		searchController.authenticationProvider = buildAuthenticationProvider(reusePreviousAuthentication);
		return searchController;
	}

	public OfferController getOfferController(boolean reusePreviousAuthentication) {
		OfferController result = new OfferController();
		result.authenticationProvider = buildAuthenticationProvider(reusePreviousAuthentication);
		result.offerService = offerController.offerService;
		result.offerTransformer = offerController.offerTransformer;
		result.offerValidator = offerController.offerValidator;
		return result;
	}

	public ItemAttachmentController getItemFileController(boolean reusePreviousAuthentication) {
		itemFileController.authenticationProvider = buildAuthenticationProvider(reusePreviousAuthentication);
		return itemFileController;
	}

	public AttachmentController getFileController(boolean reusePreviousAuthentication) {
		fileController.authenticationProvider = buildAuthenticationProvider(reusePreviousAuthentication);
		return fileController;
	}

	public ArticleController getArticleController(boolean reusePreviousAuthentication) {
		articleController.authenticationProvider = buildAuthenticationProvider(reusePreviousAuthentication);
		return articleController;
	}

}
