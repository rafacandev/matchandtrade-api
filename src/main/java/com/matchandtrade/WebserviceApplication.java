package com.matchandtrade;

import com.matchandtrade.authentication.AuthenticationServlet;
import com.matchandtrade.cli.AppCli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan(basePackageClasses=AuthenticationServlet.class)
@SpringBootApplication
@EnableAutoConfiguration()
	// Required, matchandtrade-doc-maker fails if we exclude: 
	// EmbeddedServletContainerAutoConfiguration.class
	// WebMvcAutoConfiguration.class
	// DispatcherServletAutoConfiguration.class
	
	// Classed found on the auto-configuration report Positive Matches
//	DataSourceAutoConfiguration.class
//})
public class WebserviceApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebserviceApplication.class);
	private static final String CONFIGURATION_FILE_PROPERTY_KEY = "--configFile or -cf or -Dspring.config.location";

	public static void main(String[] arguments) throws Throwable {
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

		// If line output message is interrupted (e.g: invalid command line); then, display message CommandLineOutputMessage 
		if (cli.isInterrupted()) {
			LOGGER.info(cli.getCommandLineOutputMessage());
		} else {
			// Proceed normally
			SpringApplication.run(WebserviceApplication.class);
			LOGGER.info("Using {}={}", CONFIGURATION_FILE_PROPERTY_KEY, configurationFile);
		}
	}

	private static String loadConfigurationFileProperty(AppCli cli) {
		String configurationFile = System.getProperty(CONFIGURATION_FILE_PROPERTY_KEY);
		if (configurationFile == null) {
			LOGGER.debug("Did not find {}", CONFIGURATION_FILE_PROPERTY_KEY);
		} else {
			LOGGER.debug("Found {}={}", CONFIGURATION_FILE_PROPERTY_KEY, configurationFile);
		}
		if (cli.configurationFilePath() != null) {
			LOGGER.debug("Found {}={}", CONFIGURATION_FILE_PROPERTY_KEY, cli.configurationFilePath());
			configurationFile = cli.configurationFilePath();
		}
		if (configurationFile == null) {
			LOGGER.info("Unable to start the application {} is required", CONFIGURATION_FILE_PROPERTY_KEY);
			System.exit(2);
		}
		System.setProperty("spring.config.location", configurationFile);
		return configurationFile;
	}

}
