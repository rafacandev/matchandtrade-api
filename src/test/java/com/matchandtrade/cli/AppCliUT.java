package com.matchandtrade.cli;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AppCliUT {
	
	@Test(expected=IllegalArgumentException.class)
	public void startup_When_ConfigFileIsDirectory_Then_ThrowsIllegalArgument() throws IOException {
		String directoryPath = AppCliUT.class.getProtectionDomain().getCodeSource().getLocation().getPath();		
		String[] arguments = {"-cf", directoryPath};
		new AppCli(arguments);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void startup_When_ConfigFileDoesNotExist_Then_ThrowsIllegalArgument() throws IOException {
		String[] arguments = {"-cf", "configFileTest"};
		new AppCli(arguments);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void startup_When_ConfigFileValueIsMissing_Then_ThrowsIllegalArgument() throws IOException {
		String[] arguments = {"--configFile"};
		new AppCli(arguments);
	}
	
	@Test
	public void startup_When_HasHelpArgument_Then_IsInterruptedAndOutputsHelp() throws IOException {
		String[] arguments = {"-h"};
		AppCli cli = new AppCli(arguments);
		assertTrue(cli.isInterrupted());
		assertTrue(cli.getCommandLineOutputMessage().contains("java -jar THIS_JAR.jar"));
	}

	@Test
	public void startup_When_HasEmptyArguments_Then_OutputsHelp() throws IOException {
		String[] arguments = {""};
		AppCli cli = new AppCli(arguments);
		assertTrue(cli.isInterrupted());
		assertTrue(cli.getCommandLineOutputMessage().contains("java -jar THIS_JAR.jar"));
	}

}
