package com.matchandtrade.authentication;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		AuthenticationServletTest.class,
		AuthenticationCallbakServletTest.class,
		AuthenticationOAuthGoogleTest.class
	})

public class AuthenticationIntegrationSuite {

}