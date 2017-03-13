package com.matchandtrade.config;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

public class AppConfigurationProperties {

	public enum Keys {
		AUTHENTICATION_CLIENT_ID("authentication.client.id","clientIdProperty"),
		AUTHENTICATION_CLIENT_SECRET("authentication.client.secrete","clientSecretProperty"),
		AUTHENTICATION_CLIENT_REDIRECT_URL("authentication.redirect.url","http://localhost:8080/authenticate/callback"),
		AUTHENTICATION_OAUTH_CLASS("authentication.oauth.class","com.matchandtrade.authentication.AuthenticationOAuthExistingUserMock"),
		CONFIG_FILE("matchandtrade.config.file", "src/config/matchandtrade.properties"),
		DATA_SOURCE_DRIVER_CLASS("datasource.driver.class", "org.h2.Driver"),
		DATA_SOURCE_JDBC_URL("datasource.jdbc.url", "jdbc:h2:./target/h2db/matchandtrade"),
		DATA_SOURCE_PASSWORD("datasource.password", "password"),
		DATA_SOURCE_USER("datasource.user", "username");

		private final String defaultValue;
		private final String key;

		Keys(final String key, final String defaultValue) {
	        this.key = key;
	        this.defaultValue = defaultValue;
	    }
		public String getKey() {
			return key;
		}
		public String getDefaultValue() {
			return defaultValue;
		}
		@Override
		public String toString() {
			return key + "=" + defaultValue;
		}
	}

	private Properties properties = new Properties();

	public AppConfigurationProperties(Properties appProperties) throws IOException {
		// Copy the Properties from appProperties to this.properties. Avoid assigning instance directly to avoid changes to the properties from another reference.
		for (Entry<Object, Object> e : appProperties.entrySet()) {
			properties.setProperty(e.getKey().toString(), e.getValue().toString());
		}
	}
	
	public String getProperty(Keys key) {
		String result = properties.getProperty(key.key);
		if (result == null || result.isEmpty()) {
			result = key.getDefaultValue();
		}
		return result;
	}
	
}