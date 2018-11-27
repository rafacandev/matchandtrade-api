package com.matchandtrade.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@WebAppConfiguration
@Commit
@EntityScan(basePackages = "com.matchandtrade.persistence.entity") // TODO: Review this
@TestPropertySource(locations = "file:config/matchandtrade.properties")
public @interface DefaultTestingConfiguration {

}
