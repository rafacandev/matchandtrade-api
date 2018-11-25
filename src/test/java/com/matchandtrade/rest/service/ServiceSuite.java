package com.matchandtrade.rest.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ArticleServiceIT.class,
	TradeResultServiceIT.class,
	TradeServiceIT.class
})
public class ServiceSuite {

}
