package com.matchandtrade.rest.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ArticleAttachmentServiceIT.class,
	ArticleServiceIT.class,
	TradeResultServiceIT.class,
})
public class ServiceSuite {

}
