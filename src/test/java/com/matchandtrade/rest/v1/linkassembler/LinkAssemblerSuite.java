package com.matchandtrade.rest.v1.linkassembler;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ArticleLinkAssemblerIT.class,
	SearchLinkAssemblerIT.class,
	TradeLinkAssemblerIT.class
})
public class LinkAssemblerSuite {

}
