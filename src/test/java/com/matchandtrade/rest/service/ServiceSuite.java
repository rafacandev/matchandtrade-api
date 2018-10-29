package com.matchandtrade.rest.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TradeResultServiceIT.class,
	ArticleServiceIT.class
})
public class ServiceSuite {

}
