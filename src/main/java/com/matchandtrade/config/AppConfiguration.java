package com.matchandtrade.config;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.matchandtrade.authentication.AuthenticationOAuth;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
public class AppConfiguration {
	
	private AppConfigurationProperties appProperties;

	public AppConfiguration() throws FileNotFoundException, IOException {
		appProperties = new AppConfigurationProperties(System.getProperties());
	}

	@Bean
	public ComboPooledDataSource dataSource() throws PropertyVetoException {
		ComboPooledDataSource result = new ComboPooledDataSource();
		result.setDriverClass(appProperties.getProperty(AppConfigurationProperties.Keys.DATA_SOURCE_DRIVER_CLASS));
		result.setJdbcUrl(appProperties.getProperty(AppConfigurationProperties.Keys.DATA_SOURCE_JDBC_URL));
		result.setPassword(appProperties.getProperty(AppConfigurationProperties.Keys.DATA_SOURCE_PASSWORD));
		result.setUser(appProperties.getProperty(AppConfigurationProperties.Keys.DATA_SOURCE_USER));
		return result;
	}
	
	@Bean
	public AuthenticationProperties authenticationProperties() {
		AuthenticationProperties result = new AuthenticationProperties();
		result.setClientId(appProperties.getProperty(AppConfigurationProperties.Keys.AUTHENTICATION_CLIENT_ID));
		result.setClientSecret(appProperties.getProperty(AppConfigurationProperties.Keys.AUTHENTICATION_CLIENT_SECRET));
		result.setRedirectURI(appProperties.getProperty(AppConfigurationProperties.Keys.AUTHENTICATION_CLIENT_REDIRECT_URL));
		Integer sessionTimeout = Integer.parseInt(appProperties.getProperty(AppConfigurationProperties.Keys.AUTHENTICATION_SESSION_TIMEOUT));
		result.setSessionTimeout(sessionTimeout);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Bean
	public AuthenticationOAuth authenticationOAuth() throws ReflectiveOperationException {
		Class authenticationOAuthClass = Class.forName(appProperties.getProperty(AppConfigurationProperties.Keys.AUTHENTICATION_OAUTH_CLASS));
		return (AuthenticationOAuth) authenticationOAuthClass.newInstance();
	}
	
	public AppConfigurationProperties getAppProperties() {
		return appProperties;
	}
	
}