package com.matchandtrade.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.matchandtrade.util.VersionUtil;

@Component
public class ConfigurationInfoLogger implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger("plainTextLogger");
    
    @Autowired
	private AppConfigurationProperties config;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
		VersionUtil versionUtil = new VersionUtil();

		String logo = "_  _ ____ ___ ____ _  _    ____ _  _ ___     ___ ____ ____ ___  ____ \n" +
			"|\\/| |__|  |  |    |__|    |__| |\\ | |  \\     |  |__/ |__| |  \\ |___ \n" +
			"|  | |  |  |  |___ |  |    |  | | \\| |__/     |  |  \\ |  | |__/ |___ \n";

		logger.info(logo);
		logger.info("|===========================================================");
		logger.info("|            WELCOME TO MATCH AND TRADE API");
		logger.info("| Project Name: {}", versionUtil.projectName());
		logger.info("| Project Version: {}", versionUtil.projectVersion());
		logger.info("| Build Number: {}", versionUtil.buildNumber());
		logger.info("| Build Timestamp: {}", versionUtil.buildTimestamp());
		logger.info("|");
		logger.info("| {}: {}", "authentication.oauth.class", config.authentication.getOauthClass());
		logger.info("| {}: {}", "datasource.driver.class", config.datasource.getDriverClass());
		logger.info("| {}: {}", "datasource.jdbc.url", config.datasource.getJdbcUrl());
		logger.info("| {}: {}", "--configFile -cf -Dspring.config.location", config.getConfigurationFile());
		logger.info("| {}: {}", "logging.file", config.springboot.getLoggingFile());
		logger.info("| {}: {}", "server.port", config.springboot.getServerPort());
		logger.info("|===========================================================");
    }

}
