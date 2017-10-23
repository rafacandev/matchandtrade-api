package com.matchandtrade.cli;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidParameterException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class AppCli {
	
	private boolean isInterrupted = false;
	private String commandLineOutputMessage = "";
	private CommandLine cli;
	
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
			cli = new DefaultParser().parse(options, arguments);
			if (cli.hasOption("cf")) {
				File configFile = new File(cli.getOptionValue("cf"));
				if (!configFile.exists()) {
					throw new InvalidParameterException("Configuration file [" + cli.getOptionValue("cf") + "] does not exist.");
				}
				if (configFile.isDirectory()) {
					throw new InvalidParameterException("Configuration file [" + cli.getOptionValue("cf") + "] is a directory but this application expects a file.");
				}
				if (!configFile.canRead()) {
					throw new InvalidParameterException("Configuration file [" + cli.getOptionValue("cf") + "] cannot be read.");
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

	public String configurationFilePath() {
		return cli.getOptionValue("cf");
	}
	
	public boolean isInterrupted() {
		return isInterrupted;
	}
	
	public String getCommandLineOutputMessage() {
		return commandLineOutputMessage;
	}
}
