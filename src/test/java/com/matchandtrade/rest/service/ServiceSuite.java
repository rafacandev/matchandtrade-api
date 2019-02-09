package com.matchandtrade.rest.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ArticleAttachmentServiceIT.class,
	ArticleServiceIT.class,
	AttachmentServiceIT.class,
	EssenceStorageServiceIT.class,
	MembershipServiceIT.class,
	SearchRecipeServiceIT.class,
	TradeResultServiceIT.class,
	TradeServiceIT.class
})
public class ServiceSuite {

}
