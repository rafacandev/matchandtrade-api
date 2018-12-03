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
    private final Logger log = LoggerFactory.getLogger("plainTextLogger");
    
    @Autowired
	private AppConfigurationProperties config;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
		VersionUtil versionUtil = new VersionUtil();

		String logo = "_  _ ____ ___ ____ _  _    ____ _  _ ___     ___ ____ ____ ___  ____ \n" +
			"|\\/| |__|  |  |    |__|    |__| |\\ | |  \\     |  |__/ |__| |  \\ |___ \n" +
			"|  | |  |  |  |___ |  |    |  | | \\| |__/     |  |  \\ |  | |__/ |___ \n";

		log.info(logo);
		log.info("|===========================================================");
		log.info("|            WELCOME TO MATCH AND TRADE API");
		log.info("| Project Name: {}", versionUtil.projectName());
		log.info("| Project Version: {}", versionUtil.projectVersion());
		log.info("| Build Number: {}", versionUtil.buildNumber());
		log.info("| Build Timestamp: {}", versionUtil.buildTimestamp());
		log.info("|");
		log.info("| {}: {}", "authentication.oauth.class", config.authentication.getOauthClass());
		log.info("| {}: {}", "--configFile -cf -Dspring.config.location", config.getConfigurationFile());
		log.info("| {}: {}", "datasource.driver.class", config.datasource.getDriverClass());
		log.info("| {}: {}", "datasource.jdbc.url", config.datasource.getJdbcUrl());
		log.info("| {}: {}", "essence.root.folder", config.filestorage.getEssenceRootPath());
		log.info("| {}: {}", "logging.file", config.springboot.getLoggingFile());
		log.info("| {}: {}", "server.port", config.springboot.getServerPort());
		log.info("|===========================================================");
    }

}
