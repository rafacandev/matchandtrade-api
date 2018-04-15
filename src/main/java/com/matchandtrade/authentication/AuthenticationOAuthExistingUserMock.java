
package com.matchandtrade.authentication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * OAuth2 authentication for testing and development.
 * 
 * <p>
 * Access token starting with one of the recognized {@code username} are associated with their 
 * corresponding user information. Valid {@code username}s are: {@code alice bob carol dylan elton}
 * </p>
 * <p>
 * Otherwise it will generate new random user information.
 * </p>
 * 
 * @author rafael.santos.bra@gmail.com
 */
public class AuthenticationOAuthExistingUserMock implements AuthenticationOAuth {
	
	@Autowired
	private Environment environment;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationOAuthExistingUserMock.class);

	private String buidRandomAccessToken() {
		Random random = new Random();
		return (random.nextInt(89) + 10) + "-" + (random.nextInt(89) + 10) + "-" + (random.nextInt(89) + 10) + "-" + (random.nextInt(89) + 10);
	}
	
	@Override
	public void redirectToAuthorizationAuthority(HttpServletResponse response, String state, String clientId, String redirectURI) throws AuthenticationException {
		String url = environment.getProperty("authentication.oauth.mock.url");
		if (url == null || url.length() < 1) {
			url = "http://localhost:8081/oauth/sign-in";
		}
		URI uri = null;
		try {
			uri = new URIBuilder(url)
			        .setParameter("client_id", clientId)
			        .setParameter("response_type", "code")
			        .setParameter("scope", "openid email profile" )
			        .setParameter("redirect_uri", redirectURI)
			        .setParameter("state", state)
			        .build();
		} catch (URISyntaxException e) {
			logger.error("Error building authentication URI.", e);
		}
		try {
			if (uri != null) {
				logger.info("Redirecting to {}", uri);
				response.sendRedirect(uri.toString());
			}
		} catch (IOException e) {
			logger.error("Error redirecting to Authorization Authority.", e);
			throw new AuthenticationException(e);
		}
	}

	@Override
	public AuthenticationResponsePojo obtainUserInformation(String accessToken) throws AuthenticationException {
		if (	accessToken.startsWith("alice") ||
				accessToken.startsWith("bob") ||
				accessToken.startsWith("carol") ||
				accessToken.startsWith("dylan") ||
				accessToken.startsWith("elton")) {
			String userName = accessToken.substring(0, accessToken.indexOf("-"));
			return new AuthenticationResponsePojo(null, null, userName + "@test.com", userName, accessToken);
		} else {
			long now = System.currentTimeMillis();
			String email = now + "@test.com";
			String name = now + "AuthenticationOAuthNewUserMock";
			return new AuthenticationResponsePojo(
				null,
				true,
				email,
				name,
				buidRandomAccessToken());
		}
	}

	@Override
	public String obtainAccessToken(String codeParameter, String clientId, String clientSecret, String redirectURI) throws AuthenticationException {
		logger.info("Obtaining access token for code: {}, redirectUrl: {}", codeParameter, redirectURI);
		return codeParameter + "-" + buidRandomAccessToken();
	}

}
