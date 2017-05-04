package com.matchandtrade.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationOAuthGoogle implements AuthenticationOAuth {

	private final Logger logger = LoggerFactory.getLogger(AuthenticationOAuthGoogle.class);
	
	/**
	 * Extracts the access token from the response string.
	 * @param responseString
	 * @return access token from response string.
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	private String getAccessToken(String responseString) throws IOException, JsonParseException, JsonMappingException {
		ObjectMapper jacksonObjectMapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String,Object> accessMap = jacksonObjectMapper.readValue(responseString, Map.class);
		String accessToken = (String) accessMap.get("access_token");
		return accessToken;
	}
	
	/**
	 * Returns the equivalent HttpResponse content as string.
	 * @param httpResponse
	 * @return string body representation from HttpResponse
	 * @throws IOException
	 */
	private String getResponse(HttpResponse httpResponse) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
		StringBuilder responseString = new StringBuilder();
		String line;
		while((line = reader.readLine()) != null) {
			responseString.append(line);
		}
		return responseString.toString();
	}
	
	@Override
	public String obtainAccessToken(String codeParameter, String clientId, String clientSecret, String redirectURI) throws AuthenticationException {
		// Build access token URI
		URI uri = null;
		try {
			uri = new URIBuilder()
			        .setScheme("https")
			        .setHost("www.googleapis.com")
			        .setPath("/oauth2/v4/token")
			        .setParameter("code", codeParameter)
			        .setParameter("client_id", clientId)
			        .setParameter("client_secret", clientSecret)
			        .setParameter("redirect_uri", redirectURI)
			        .setParameter("grant_type", "authorization_code" )
			        .build();
		} catch (URISyntaxException e) {
			throw new AuthenticationException(e);
		}
		
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse;
		try {
			// Execute the HTTP POST
			httpResponse = httpClientExecute(httpPost, httpClient);
			// Get HTTP Request as string
			String responseString = getResponse(httpResponse);
			httpResponse.close();
			logger.debug("Access token response body {}.", responseString);
			// Get the 'access_token' from responseString
			String accessToken = getAccessToken(responseString);
			if (accessToken == null) {
				throw new AuthenticationException("access_token parameter not found on response body: [" + responseString + "].");
			}
			return accessToken;
		} catch (UnsupportedOperationException | IOException e) {
			throw new AuthenticationException("Not able to obtain access token. " + e.getMessage());
		}
	}

	CloseableHttpResponse httpClientExecute(HttpRequestBase httpRequest, CloseableHttpClient httpClient) throws IOException, ClientProtocolException {
		logger.debug("Sending HTTP POST request to {}.", httpRequest.getURI() );
		CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
		return httpResponse;
	}

	@Override
	public AuthenticationResponsePojo obtainUserInformation(String accessToken) throws AuthenticationException {
		// Build user information URI
		URI uri = null;
		try {
			uri = new URIBuilder()
			        .setScheme("https")
			        .setHost("www.googleapis.com")
			        .setPath("/userinfo/v2/me")
			        .build();
		} catch (URISyntaxException e) {
			throw new AuthenticationException(e);
		}
		
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("Authorization", "Bearer " + accessToken);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse;
		try {
			// Execute the HTTP request
			httpResponse = httpClientExecute(httpGet, httpClient);
			// Get the responseString
			String responseString = getResponse(httpResponse);
			httpResponse.close();
			logger.debug("User information response body {} for access token {}.", responseString, accessToken);

			// Get the user information from responseString
			ObjectMapper jacksonObjectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String,Object> userInfoMap = jacksonObjectMapper.readValue(responseString, Map.class);
			AuthenticationResponsePojo result = new AuthenticationResponsePojo(
					null, 
					null, 
					userInfoMap.get("email").toString(), 
					userInfoMap.get("name").toString(), 
					accessToken);
			return result;
		} catch (UnsupportedOperationException | IOException e) {
			throw new AuthenticationException(e);
		}
	}

	public void redirectToAuthorizationAuthority(HttpServletResponse response, String state, String clientId, String redirectURI) throws AuthenticationException {
		// 2. Send an authentication request to Google
		URI uri = null;
		try {
			uri = new URIBuilder()
			        .setScheme("https")
			        .setHost("accounts.google.com")
			        .setPath("/o/oauth2/v2/auth")
			        .setParameter("client_id", clientId)
			        .setParameter("response_type", "code")
			        .setParameter("scope", "openid email profile" )
			        .setParameter("redirect_uri", redirectURI)
			        .setParameter("state", state)
			        .build();
		} catch (URISyntaxException e) {
			logger.error("Error building redirect URI to authorizationAuthority.", e);
			throw new AuthenticationException(e);
		}
		try {
			logger.debug("Redirecting to authorization authority at URL {}.", uri.toURL());
			response.sendRedirect(uri.toURL().toString());
		} catch (IOException e) {
			throw new AuthenticationException(e);
		}
	}
}
