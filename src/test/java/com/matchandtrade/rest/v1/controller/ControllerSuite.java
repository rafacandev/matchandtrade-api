package com.matchandtrade.rest.v1.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	AuthenticationControllerIT.class,
	UserControllerIT.class,
	TradeControllerIT.class,
	MembershipControllerIT.class,
	TradeResultControllerIT.class,
	ListingControllerIT.class,
	ArticleAttachmentControllerIT.class

})
public class ControllerSuite {

}
