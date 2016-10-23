package com.matchandtrade.rest.v1.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@RunWith(Suite.class)
@ContextConfiguration(locations = "/application-context-test.xml")
@SuiteClasses({
	UserControllerGetIT.class
})
public class UserControllerSuite {

}
