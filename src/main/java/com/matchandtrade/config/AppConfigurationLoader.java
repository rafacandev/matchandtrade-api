package com.matchandtrade.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Map.Entry;

public class AppConfigurationLoader {

	private static boolean isConfigurationLoadedFromFile = false;
	
	// Utility classes should not have public constructors
	private AppConfigurationLoader() { }

	public static boolean isConfigurationLoadedFromFile() {
		return isConfigurationLoadedFromFile;
	}
	
	public static void loadConfigurationFromFilePath(final String configFilePath) throws IOException, FileNotFoundException {
		File configFile = new File(configFilePath);
		if (!configFile.exists()) {
			throw new IOException("File does not exist: " + configFile.getAbsolutePath());
		} else if (configFile.isDirectory()) {
			throw new IOException("The path provided is a directory instead of a valid file: " + configFile.getAbsolutePath());
		} else {
			System.setProperty(AppConfigurationProperties.Keys.CONFIG_FILE.getKey(), configFile.getAbsolutePath());
			// Load the content of the configuration file as system properties
			Properties additionalProperties = new Properties();
			additionalProperties.load(new FileInputStream(configFile));
			for(Entry<Object, Object> e : additionalProperties.entrySet()) {
				System.setProperty(e.getKey().toString(), e.getValue().toString());
			}
			isConfigurationLoadedFromFile = true;
		}
	}
	
}
