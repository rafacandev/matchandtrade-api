package com.matchandtrade.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.matchandtrade.util.VersionUtil;

@Component
public class ConfigurationInfoLogger implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger("plainTextLogger");
    
    @Autowired
    private Environment environment;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
		VersionUtil versionUtil = new VersionUtil();
		logger.info("|===========================================================");
		logger.info("| WELCOME TO MATCH AND TRADE API");
		logger.info("|");
		logger.info("| Project Name: {}", versionUtil.projectName());
		logger.info("| Project Version: {}", versionUtil.projectVersion());
		logger.info("| Build Number: {}", versionUtil.buildNumber());
		logger.info("| Build Timestamp: {}", versionUtil.buildTimestamp());
		logger.info("|");
		logger.info("| {}: {}", MatchAndTradePropertyKeys.AUTHENTICATION_OAUTH_CLASS.toString(), environment.getProperty(MatchAndTradePropertyKeys.AUTHENTICATION_OAUTH_CLASS.toString()));
		logger.info("| {}: {}", MatchAndTradePropertyKeys.DATA_SOURCE_JDBC_URL.toString(), environment.getProperty(MatchAndTradePropertyKeys.DATA_SOURCE_JDBC_URL.toString()));
		logger.info("| {}: {}", MatchAndTradePropertyKeys.LOGGING_FILE.toString(), environment.getProperty(MatchAndTradePropertyKeys.LOGGING_FILE.toString()));
		logger.info("| {}: {}", MatchAndTradePropertyKeys.CONFIG_FILE.toString(), environment.getProperty(MatchAndTradePropertyKeys.CONFIG_FILE.toString()));
		logger.info("| {}: {}", MatchAndTradePropertyKeys.ESSENCE_STORAGE_ROOT_FOLDER.toString(), environment.getProperty(MatchAndTradePropertyKeys.ESSENCE_STORAGE_ROOT_FOLDER.toString()));
		logger.info("| {}: {}", MatchAndTradePropertyKeys.SERVER_PORT.toString(), environment.getProperty(MatchAndTradePropertyKeys.SERVER_PORT.toString()));
		logger.info("|===========================================================");
    }
}
