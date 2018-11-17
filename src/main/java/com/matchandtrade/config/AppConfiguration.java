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
	private AppConfigurationProperties configProperties;

	@Bean
	public ComboPooledDataSource dataSource() throws PropertyVetoException {
		ComboPooledDataSource result = new ComboPooledDataSource();
		result.setDriverClass(configProperties.datasource.getDriverClass());
		result.setJdbcUrl(configProperties.datasource.getJdbcUrl());
		result.setPassword(configProperties.datasource.getPassword());
		result.setUser(configProperties.datasource.getUser());
		return result;
	}
	
	@Bean
	public AuthenticationOAuth authenticationOAuth() throws ReflectiveOperationException {
		Class<?> authenticationOAuthClass = Class.forName(configProperties.authentication.getOauthClass());
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