package com.matchandtrade.rest.v1.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	FileControllerPostIT.class,
	ItemFileControllerPostIT.class,
	ItemFileControllerGetIT.class,
	ItemFileControllerDeleteIT.class
})
public class FileSuite {

}
