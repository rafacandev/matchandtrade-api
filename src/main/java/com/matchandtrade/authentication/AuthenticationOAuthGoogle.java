package com.matchandtrade.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationOAuthGoogle implements AuthenticationOAuth {
	
	
	@Override
	public String obtainAccessToken(String codeParameter, String clientId, String clientSecret, String redirectURI) throws AuthenticationException {
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
			System.err.println("Error building uri. Exception message: " + e.getMessage() );
			throw new AuthenticationException(e);
		}
		
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			CloseableHttpResponse httpPostResponse = httpClient.execute(httpPost);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpPostResponse.getEntity().getContent()));
			StringBuilder responseString = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null) {
				responseString.append(line);
			}
			System.out.println(responseString.toString());
			
			// Returns only the 'access_token' from responseString
			ObjectMapper jacksonObjectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String,Object> accessMap = jacksonObjectMapper.readValue(responseString.toString(), Map.class);
			String accessToken = (String) accessMap.get("access_token");
			return accessToken;
		} catch (UnsupportedOperationException | IOException e) {
			System.out.println("Not able to obtain access token." + e.getMessage());
			throw new AuthenticationException(e);
		}
	}

	@Override
	public UserAuthentication obtainUserInformation(String accessToken) throws AuthenticationException {
		URI uri = null;
		try {
			uri = new URIBuilder()
			        .setScheme("https")
			        .setHost("www.googleapis.com")
			        .setPath("/userinfo/v2/me")
			        .build();
		} catch (URISyntaxException e) {
			System.err.println("Error building uri. Exception message: " + e.getMessage() );
		}
		
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("Authorization", "Bearer " + accessToken);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			CloseableHttpResponse httpPostResponse;
			httpPostResponse = httpClient.execute(httpGet);
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpPostResponse.getEntity().getContent()));

			StringBuilder respString = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null) {
				respString.append(line);
			}
			System.out.println(respString.toString());
			
			ObjectMapper jacksonObjectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String,Object> userInfoMap = jacksonObjectMapper.readValue(respString.toString(), Map.class);

			UserAuthentication result = new UserAuthentication();
			result.setEmail(userInfoMap.get("email").toString());
			result.setName(userInfoMap.get("name").toString());
			return result;
		} catch (UnsupportedOperationException | IOException e) {
			throw new AuthenticationException(e);
		}
	}

	public void redirectToAuthorizationServer(HttpServletResponse response, String state, String clientId, String redirectURI) throws AuthenticationException {
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
			System.err.println("Error building uri. Exception message: " + e.getMessage() );
		}
		try {
			System.out.println(uri.toURL().toString());
			response.sendRedirect(uri.toURL().toString());
		} catch (IOException e) {
			throw new AuthenticationException(e);
		}
	}
}
