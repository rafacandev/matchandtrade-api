package com.matchandtrade.authentication;

import com.matchandtrade.config.AppConfigurationProperties;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class MockConfigurationProperties {
	public static final String AUTHENTICATION_CLIENT_ID = "clientId";
	public static final String AUTHENTICATION_REDIRECT_URL = "http://redirecturl.com";

	public static AppConfigurationProperties buildConfigProperties() {
		AppConfigurationProperties configPropertiesMock = Mockito.mock(AppConfigurationProperties.class);
		AppConfigurationProperties.Authentication authenticationMock = Mockito.mock(AppConfigurationProperties.Authentication.class);
		when(authenticationMock.getClientId()).thenReturn(AUTHENTICATION_CLIENT_ID);
		when(authenticationMock.getRedirectUrl()).thenReturn(AUTHENTICATION_REDIRECT_URL);
		configPropertiesMock.authentication = authenticationMock;
		return configPropertiesMock;
	}
}