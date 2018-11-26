package com.matchandtrade;

import com.matchandtrade.cli.AppCli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: Test this removal
//@ServletComponentScan(basePackageClasses=AuthenticationServlet.class)
@SpringBootApplication
@EnableAutoConfiguration()
public class WebserviceApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebserviceApplication.class);
	private static final String CONFIGURATION_FILE_PROPERTY_DESCRIPTION = "--configFile or -cf or -Dspring.config.location";
	private static final String CONFIGURATION_FILE_PROPERTY_KEY = "spring.config.location";

	public static void main(String[] arguments) {
		// Handles the command line options.
		AppCli cli = null;
		try {
			cli = new AppCli(arguments);
		} catch (Exception e) {
			LOGGER.info("Not able to start application! ", e.getMessage(), e);
			System.exit(1);
		}

		// Load the correct configuration file as an environment property
		String configurationFile = loadConfigurationFileProperty(cli);

		if (configurationFile == null || cli.isInterrupted()) {
			LOGGER.debug("Did not find {}", CONFIGURATION_FILE_PROPERTY_DESCRIPTION);
			LOGGER.info(cli.getCommandLineOutputMessage());
		} else {
			// Proceed normally
			SpringApplication.run(WebserviceApplication.class);
		}
	}

	private static String loadConfigurationFileProperty(AppCli cli) {
		String configurationFile;
		if (cli.configurationFilePath() != null) {
			LOGGER.debug("Found {}={}", CONFIGURATION_FILE_PROPERTY_DESCRIPTION, cli.configurationFilePath());
			configurationFile = cli.configurationFilePath();
			System.setProperty(CONFIGURATION_FILE_PROPERTY_KEY, configurationFile);
		} else {
			configurationFile = System.getenv(CONFIGURATION_FILE_PROPERTY_KEY);
			if (configurationFile == null) {
				configurationFile = System.getProperty(CONFIGURATION_FILE_PROPERTY_KEY);
			}
		}
		return configurationFile;
	}

}
