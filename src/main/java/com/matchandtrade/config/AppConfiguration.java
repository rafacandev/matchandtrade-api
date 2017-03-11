package com.matchandtrade.config;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.matchandtrade.authentication.AuthenticationOAuth;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
public class AppConfiguration {
	
	public static String CONFIG_FILE = "src/config/matchandtrade.properties";
	
	private AppConfigurationProperties properties;

	String dataSourceDriverClass = "org.h2.Driver";
	String dataSourceJdbcUrl = "jdbc:h2:./target/h2db/matchandtrade";
	String dataSourceUser = "username";
	String dataSourcePassword = "password";

	public AppConfiguration() throws FileNotFoundException, IOException {
		properties = new AppConfigurationProperties(new FileInputStream(CONFIG_FILE));
	}

	@Bean
	public ComboPooledDataSource dataSource() throws PropertyVetoException {
		ComboPooledDataSource result = new ComboPooledDataSource();
		result.setDriverClass(properties.getProperty(AppConfigurationProperties.Keys.DATA_SOURCE_DRIVER_CLASS));
		result.setJdbcUrl(properties.getProperty(AppConfigurationProperties.Keys.DATA_SOURCE_JDBC_URL));
		result.setPassword(properties.getProperty(AppConfigurationProperties.Keys.DATA_SOURCE_PASSWORD));
		result.setUser(properties.getProperty(AppConfigurationProperties.Keys.DATA_SOURCE_USER));
		return result;
	}
	
	@Bean
	public AuthenticationProperties authenticationProperties() {
		AuthenticationProperties result = new AuthenticationProperties();
		result.setClientId(properties.getProperty(AppConfigurationProperties.Keys.AUTHENTICATION_CLIENT_ID));
		result.setClientSecret(properties.getProperty(AppConfigurationProperties.Keys.AUTHENTICATION_CLIENT_SECRET));
		result.setRedirectURI(properties.getProperty(AppConfigurationProperties.Keys.AUTHENTICATION_CLIENT_REDIRECT_URL));
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Bean
	public AuthenticationOAuth authenticationOAuth() throws ClassNotFoundException, InstantiationException, ReflectiveOperationException {
		Class authenticationOAuthClass = Class.forName(properties.getProperty(AppConfigurationProperties.Keys.AUTHENTICATION_OAUTH_CLASS));
		AuthenticationOAuth result = (AuthenticationOAuth) authenticationOAuthClass.newInstance();
		return result;
	}
}