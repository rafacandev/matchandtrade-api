package com.matchandtrade.config;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

/**
 * 
 * Loads properties before spring application context starts.
 * 
 * @see META-INF/spring.factories
 * @see https://docs.spring.io/spring-boot/docs/current/reference/html/howto-spring-boot-application.html
 * @author rafael.santos.bra@gmail.com
 */
public class EnvironmentPropertiesPostProcessor implements EnvironmentPostProcessor {

	private final PropertiesPropertySourceLoader loader = new PropertiesPropertySourceLoader();
	
	private void loadConfigFileProperties(ConfigurableEnvironment environment, String configFilePath) {
		if (configFilePath != null) {
			Resource configFileResource = new PathResource(configFilePath);
			PropertySource<?> propertySource = loadPropertyResource("matchandtrade-properties", configFileResource);
			environment.getPropertySources().addFirst(propertySource);
		}
	}

	private PropertySource<?> loadPropertyResource(String propertyName, Resource path) {
		if (!path.exists()) {
			throw new IllegalArgumentException(propertyName + "=" + path + " . File does not exist.");
		}
		try {
			return this.loader.load(propertyName, path, null);
		}
		catch (IOException e) {
			throw new IllegalStateException("Failed to load " + propertyName + " from " + path, e);
		}
	}
	
	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		String configFilePath = System.getProperty(MatchAndTradePropertyKeys.CONFIG_FILE.toString());
		loadConfigFileProperties(environment, configFilePath);
	}

}