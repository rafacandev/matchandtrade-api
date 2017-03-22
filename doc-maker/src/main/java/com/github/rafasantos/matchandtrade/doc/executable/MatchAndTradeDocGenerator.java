package com.github.rafasantos.matchandtrade.doc.executable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rafasantos.matchandtrade.doc.generator.OutputGenerator;
import com.github.rafasantos.matchandtrade.doc.generator.rest.RestAuthenticate;
import com.github.rafasantos.matchandtrade.doc.generator.rest.RestAuthentication;
import com.matchandtrade.WebserviceApplication;


// mvn exec:java -Dexec.mainClass="com.github.rafasantos.matwebtest.DocumentationGenerator"
public class MatchAndTradeDocGenerator {
	
	private static final Logger logger = LoggerFactory.getLogger(MatchAndTradeDocGenerator.class);

	public static void main(String[] args) {
		try {
			logger.info("Starting Match and Trade web server.");
			startMatchAndTradeWebServer();
			MatchAndTradeDocGenerator mainInstance = new MatchAndTradeDocGenerator();
			mainInstance.execute();
			logger.info("Document generation complete.");
			System.exit(0);
		} catch (Exception e) {
			logger.error("Exection interrupted. Exception message: {}", e.getMessage(), e);
			System.exit(-1);
		} finally {
			System.exit(0);
		}
	}
	
	public void execute() throws IOException {
		// Generate README.md
		String readmeLocation = MatchAndTradeDocGenerator.class.getClassLoader().getResource("doc/README.md").getPath();
		File readmeFile = new File(readmeLocation);
		// TODO do not hard code this
		File readmeDestination = new File("target" + File.separator + "matchandtrade-doc" + File.separator + "README.md");
		FileUtils.copyFile(readmeFile, readmeDestination);
		
		// TODO Scan all files instead of instantiate one by one manually
		List<OutputGenerator> generators = new ArrayList<OutputGenerator>();
		generators.add(new RestAuthenticate());
		generators.add(new RestAuthentication());
		
		for(OutputGenerator t : generators) {
			t.execute();
			String tOutput = t.getDocOutput();
			String tOutputLocation = t.getDocOutputLocation();
			// TODO Do not hard code this
			String outputRootLocationString = "target";
			File tOutputFile = new File(outputRootLocationString + File.separator + "matchandtrade-doc" + File.separator + tOutputLocation);
			FileUtils.write(tOutputFile, tOutput, StandardCharsets.UTF_8);
		}
		
	}

	private static void startMatchAndTradeWebServer() throws IOException {
		String[] arguments = buildArguments();
		PropertiesProvider.getInstance().buildAppProperties(arguments);
		WebserviceApplication.main(arguments);
	}

	private static String[] buildArguments() {
		URL location = MatchAndTradeDocGenerator.class.getProtectionDomain().getCodeSource().getLocation();
		String configFilePath = location.getPath() + "matchandtrade.properties";
		String[] arguments = {"-cf", configFilePath};
		return arguments;
	}

}
