package com.matchandtrade.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.security.InvalidParameterException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.config.AppConfiguration;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AppCliUT {
	
	@Test
	public void configFilePostitive() throws FileNotFoundException {
		String filePath = AppCliUT.class.getProtectionDomain().getCodeSource().getLocation().getPath()
				+ AppCliUT.class.getName().replace(".", "/")
				+ ".class";
		String[] arguments = {"-cf", filePath};
		AppCli cli = new AppCli(arguments);
		assertEquals(filePath, AppConfiguration.CONFIG_FILE);
		assertEquals(false, cli.isInterrupted());
	}
	
	@Test(expected=FileNotFoundException.class)
	public void configFileNegativeDirectory() throws FileNotFoundException {
		String directoryPath = AppCliUT.class.getProtectionDomain().getCodeSource().getLocation().getPath();		
		String[] arguments = {"-cf", directoryPath};
		AppCli cli = new AppCli(arguments);
		assertEquals(true, cli.isInterrupted());
	}
	
	@Test(expected=FileNotFoundException.class)
	public void configFileNegativeFileNotFound() throws FileNotFoundException {
		String[] arguments = {"-cf", "configFileTest"};
		AppCli cli = new AppCli(arguments);
		assertEquals(true, cli.isInterrupted());
	}
	
	@Test(expected=InvalidParameterException.class)
	public void configFileNegativeMissingArgument() throws FileNotFoundException {
		String[] arguments = {"--configFile"};
		AppCli cli = new AppCli(arguments);
		assertEquals(true, cli.isInterrupted());
	}
	
	@Test
	public void help() throws FileNotFoundException {
		String[] arguments = {"-h"};
		AppCli cli = new AppCli(arguments);
		assertEquals(true, cli.isInterrupted());
		assertTrue(cli.getCommandLineOutputMessage().length()>0);
	}

}
