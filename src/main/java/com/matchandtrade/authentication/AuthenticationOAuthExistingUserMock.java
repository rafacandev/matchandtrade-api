package com.matchandtrade.authentication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used for testing and development purposes. It will not send an
 * authentication request to the authentication authority. Instead it is going
 * to return to "redirectURI" allowing quick development and testing.
 * 
 * @author rafael.santos.bra@gmail.com
 */
public class AuthenticationOAuthExistingUserMock implements AuthenticationOAuth {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationOAuthExistingUserMock.class);
	public static final String EMAIL = "testing.email@test.com";
	public static final String NAME = "AuthenticationOAuthExistingUserMock";

	private String buidRandomAccessToken() {
		Random random = new Random();
		String token = random.nextInt(9999) + "-" +random.nextInt(9999) + "-" +random.nextInt(9999);
		return token;
	}
	
	@Override
	public void redirectToAuthorizationAuthority(HttpServletResponse response, String state, String clientId, String redirectURI) throws AuthenticationException {
		URI uri = null;
		try {
			uri = new URIBuilder(redirectURI)
			        .setParameter("client_id", clientId)
			        .setParameter("response_type", "code")
			        .setParameter("scope", "openid email profile" )
			        .setParameter("redirect_uri", redirectURI)
			        .setParameter("state", state)
			        .build();
		} catch (URISyntaxException e) {
			logger.error("Error building authentication URI.", e);
		}
		logger.info("Redirecting to {}", uri.toString());
		try {
			response.sendRedirect(uri.toString());
		} catch (IOException e) {
			logger.error("Error redirecting to Authorization Authority.", e);
			throw new AuthenticationException(e);
		}
	}

	@Override
	public AuthenticationResponseJson obtainUserInformation(String accessToken) throws AuthenticationException {
		AuthenticationResponseJson result = new AuthenticationResponseJson(
				null,
				null,
				EMAIL,
				NAME,
				buidRandomAccessToken());
		return result;
	}

	@Override
	public String obtainAccessToken(String codeParameter, String clientId, String clientSecret, String redirectURI)
			throws AuthenticationException {
		return buidRandomAccessToken();
	}

}
