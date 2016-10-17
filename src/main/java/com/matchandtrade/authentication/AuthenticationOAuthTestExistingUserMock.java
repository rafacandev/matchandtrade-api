package com.matchandtrade.authentication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.URIBuilder;

//import matchingtrade.persistence.dao.UserDao;

/**
 * This class is used for testing and development purposes. It will not send an
 * authentication request to the authentication authority. Instead it is going
 * to return to "redirectURI" allowing development and
 * testing without doing to the authentication authority.
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
public class AuthenticationOAuthTestExistingUserMock implements AuthenticationOAuth {
//
//	@Autowired
//	UserDao userDao;

	@Override
	public void redirectToAuthorizationServer(HttpServletResponse response, String state, String clientId, String redirectURI) throws AuthenticationException {
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
			System.err.println("Error building uri. Exception message: " + e.getMessage() );
		}
		System.out.println("Redirecting to " + uri.toString());
		try {
			response.sendRedirect(uri.toString());
		} catch (IOException e) {
			throw new AuthenticationException(e);
		}
	}

	@Override
	public UserAuthentication obtainUserInformation(String accessToken) throws AuthenticationException {
		UserAuthentication result = new UserAuthentication();
		result.setAuthenticated(true);
		result.setEmail(accessToken + "@mock.com");
		result.setName(accessToken);
		result.setNewUser(true);
		return result;
	}

	@Override
	public String obtainAccessToken(String codeParameter, String clientId, String clientSecret, String redirectURI)
			throws AuthenticationException {
		return "AuthenticationOAuthTestExistingUserMock";
	}

}
