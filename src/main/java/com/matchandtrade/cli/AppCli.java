package com.matchandtrade.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidParameterException;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.matchandtrade.config.AppConfigurationProperties;

public class AppCli {
	
	private boolean isInterrupted = false;
	private String commandLineOutputMessage = "";

	public AppCli(String[] arguments) throws IOException {
		
		Options options = new Options();

		options.addOption( Option.builder("cf")
				.longOpt("configFile")
				.required(false)
				.hasArg(true)
				.argName("PATH_TO_CONFIG_FILE")
				.desc("The path for the configuration file normally called matchandtrade.properties")
				.build());
		
		options.addOption( Option.builder("h")
				.longOpt("help")
				.required(false)
				.hasArg(false)
				.desc("Display the help information")
				.build());

		try {
			CommandLine cli = new DefaultParser().parse(options, arguments);
			if (cli.hasOption("cf")) {
				String configFilePath = cli.getOptionValue("cf");
				File configFile = new File(configFilePath);
				if (!configFile.exists()) {
					throw new IOException("File does not exist: " + configFile.getAbsolutePath());
				} else if (configFile.isDirectory()) {
					throw new IOException("The path provided is a directory instead of a valid file: " + configFile.getAbsolutePath());
				} else if (!configFile.isFile()) {
					throw new IOException("The path provided is not a file: " + configFile.getAbsolutePath());
				} else {
					System.setProperty(AppConfigurationProperties.Keys.CONFIG_FILE.getKey(), configFilePath);
					// Load the content of the configuration file as system properties
					Properties additionalProperties = new Properties();
					additionalProperties.load(new FileInputStream(configFile));
					for(Entry<Object, Object> e : additionalProperties.entrySet()) {
						System.setProperty(e.getKey().toString(), e.getValue().toString());
					}
				}
			}
			
			if (cli.hasOption("h")) {
				StringWriter stringWritter = new StringWriter();
				PrintWriter printWritter = new PrintWriter(stringWritter);
				
				isInterrupted = true;
				HelpFormatter formatter = new HelpFormatter();
				formatter.setWidth(150);
				formatter.printHelp(printWritter, 
						150, // Width
						"java -jar THIS_JAR.jar", // Usage
						"Match and Trade command line help\n", // Header 
						options, // Options
						3, // Left pad
						3, // Description pad
						"\n https://github.com/rafasantos/matchandtrade \n" // Footer
						);
				commandLineOutputMessage = stringWritter.toString();
			}
		} catch (ParseException e) {
			isInterrupted = true;
			throw new InvalidParameterException(e.getMessage());
		}
	}
	
	public boolean isInterrupted() {
		return isInterrupted;
	}
	
	public String getCommandLineOutputMessage() {
		return commandLineOutputMessage;
	}
}
