package com.matchandtrade.rest.v1.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ArticleAttachmentControllerIT.class,
	ArticleControllerIT.class,
	AuthenticationControllerIT.class,
	ListingControllerIT.class,
	MembershipControllerIT.class,
	OfferControllerIT.class,
	SearchControllerIT.class,
	TradeControllerIT.class,
	TradeResultControllerIT.class,
	UserControllerIT.class,
})
public class ControllerSuite {

}
