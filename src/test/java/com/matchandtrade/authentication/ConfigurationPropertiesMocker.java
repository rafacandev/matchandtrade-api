package com.matchandtrade.authentication;

import com.matchandtrade.config.AppConfigurationProperties;
import org.mockito.Mockito;

public class ConfigurationPropertiesMocker {

	public static final String AUTHENTICATION_CALLBACK_URL = "http://callbackurl.com";
	public static final String AUTHENTICATION_CLIENT_ID = "clientId";
	public static final String AUTHENTICATION_CLIENT_SECRET = "clientSecret";
	public static final String AUTHENTICATION_REDIRECT_URL = "http://redirecturl.com";

	public static AppConfigurationProperties buildConfigProperties() {
		AppConfigurationProperties configPropertiesMock = Mockito.mock(AppConfigurationProperties.class);
		AppConfigurationProperties.Authentication authenticationMock = Mockito.mock(AppConfigurationProperties.Authentication.class);
		Mockito.doReturn(AUTHENTICATION_CLIENT_ID).when(authenticationMock).getClientId();
		Mockito.doReturn(AUTHENTICATION_CLIENT_SECRET).when(authenticationMock).getClientSecret();
		Mockito.doReturn(AUTHENTICATION_REDIRECT_URL).when(authenticationMock).getRedirectUrl();
		Mockito.doReturn(AUTHENTICATION_CALLBACK_URL).when(authenticationMock).getCallbackUrl();
		configPropertiesMock.authentication = authenticationMock;
		return configPropertiesMock;
	}

}