package com.matchandtrade.rest.v1.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@RunWith(Suite.class)
@SuiteClasses({
	UserControllerIT.class,
	TradeControllerIT.class,
	MembershipControllerIT.class,
	TradeResultControllerIT.class
})
public class ControllerSuite {

}
