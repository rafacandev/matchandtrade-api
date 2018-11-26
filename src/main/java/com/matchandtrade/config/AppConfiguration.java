package com.matchandtrade.config;

import com.matchandtrade.authentication.AuthenticationOAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Configuration
public class AppConfiguration {
	@Autowired
	private AppConfigurationProperties configProperties;

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