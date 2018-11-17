package com.matchandtrade.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class AppConfigurationProperties {

	@Autowired
	public Authentication authentication;
	@Value("${spring.config.location:}")
	private String configurationFile;
	@Autowired
	public Datasource datasource;
	@Autowired
	public FileStorage filestorage;
	@Autowired
	public Springboot springboot;

	@Component
	public static class Authentication {
		@Value("${authentication.client.id}")
		private String clientId;
		@Value("${authentication.client.secret}")
		private String clientSecret;
		@Value("${authentication.redirect.url}")
		private String redirectUrl;
		@Value("${authentication.callback.url}")
		private String callbackUrl;
		@Value("${authentication.session.timeout}")
		private int sessionTimeout;
		@Value("${authentication.oauth.class}")
		private String oauthClass;
		@Value("${authentication.oauth.mock.url:http://localhost:8081/oauth/sign-in}")
		private String mockUrl;

		public String getClientId() {
			return clientId;
		}

		public String getClientSecret() {
			return clientSecret;
		}

		public String getRedirectUrl() {
			return redirectUrl;
		}

		public String getCallbackUrl() {
			return callbackUrl;
		}

		public int getSessionTimeout() {
			return sessionTimeout;
		}

		public String getOauthClass() {
			return oauthClass;
		}

		public String getMockUrl() {
			return mockUrl;
		}
	}

	@Component
	public static class Datasource {
		@Value("${datasource.driver.class}")
		private String driverClass;
		@Value("${datasource.jdbc.url}")
		private String jdbcUrl;
		@Value("${datasource.password}")
		private String password;
		@Value("${datasource.user}")
		private String user;

		public String getDriverClass() {
			return driverClass;
		}

		public String getJdbcUrl() {
			return jdbcUrl;
		}

		public String getPassword() {
			return password;
		}

		public String getUser() {
			return user;
		}
	}

	@Component
	public static class FileStorage {
		@Value("${essence.root.folder}")
		private String essenceRootPath;

		public String getEssenceRootPath() {
			return essenceRootPath;
		}
	}

	@Component
	public static class Springboot {
		@Value("${logging.file}")
		private String loggingFile;
		@Value("${server.port}")
		private int serverPort;

		public String getLoggingFile() {
			return loggingFile;
		}

		public int getServerPort() {
			return serverPort;
		}
	}

	public String getConfigurationFile() {
		return configurationFile;
	}

}
