package com.matchandtrade.rest.v1.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	UserControllerGetIT.class,
	UserControllerPutIT.class,
	UserControllerSearchIT.class
})
public class UserControllerSuite {

}
