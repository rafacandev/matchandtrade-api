package com.matchandtrade.config;

import java.beans.PropertyVetoException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.matchandtrade.authentication.AuthenticationOAuth;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
public class AppConfiguration {
	
	@Autowired
	private Environment environment;

	@Bean
	public ComboPooledDataSource dataSource() throws PropertyVetoException {
		ComboPooledDataSource result = new ComboPooledDataSource();
		result.setDriverClass(environment.getProperty(MatchAndTradePropertyKeys.DATA_SOURCE_DRIVER_CLASS.toString()));
		result.setJdbcUrl(environment.getProperty(MatchAndTradePropertyKeys.DATA_SOURCE_JDBC_URL.toString()));
		result.setPassword(environment.getProperty(MatchAndTradePropertyKeys.DATA_SOURCE_PASSWORD.toString()));
		result.setUser(environment.getProperty(MatchAndTradePropertyKeys.DATA_SOURCE_USER.toString()));
		return result;
	}
	
	@Bean
	public AuthenticationProperties authenticationProperties() {
		AuthenticationProperties result = new AuthenticationProperties();
		result.setClientId(environment.getProperty(MatchAndTradePropertyKeys.AUTHENTICATION_CLIENT_ID.toString()));
		result.setClientSecret(environment.getProperty(MatchAndTradePropertyKeys.AUTHENTICATION_CLIENT_SECRET.toString()));
		result.setRedirectURI(environment.getProperty(MatchAndTradePropertyKeys.AUTHENTICATION_CLIENT_REDIRECT_URL.toString()));
		result.setCallbackUrl(environment.getProperty(MatchAndTradePropertyKeys.AUTHENTICATION_CLIENT_CALLBACK_URL.toString()));
		Integer sessionTimeout = Integer.parseInt(environment.getProperty(MatchAndTradePropertyKeys.AUTHENTICATION_SESSION_TIMEOUT.toString()));
		result.setSessionTimeout(sessionTimeout);
		return result;
	}

	@Bean
	public AuthenticationOAuth authenticationOAuth() throws ReflectiveOperationException {
		Class<?> authenticationOAuthClass = Class.forName(environment.getProperty(MatchAndTradePropertyKeys.AUTHENTICATION_OAUTH_CLASS.toString()));
		return (AuthenticationOAuth) authenticationOAuthClass.newInstance();
	}
	
	@Bean
	public ServletContextInitializer servletContextInitializer() {
	    return new ServletContextInitializer() {
			@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
				servletContext.getSessionCookieConfig().setName("MTSESSION");
			}
	    };
	}
	
}