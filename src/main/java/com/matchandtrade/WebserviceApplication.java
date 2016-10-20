package com.matchandtrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ImportResource("classpath:application-context-spring-boot.xml")
@EnableSwagger2
@ServletComponentScan
@SpringBootApplication
public class WebserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebserviceApplication.class, args);
	}
	
    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("matchandtrade")
                .pathMapping("/swagger")
                .apiInfo(swaggerApiInfo())
                .select()
                .paths(swaggerApiPaths())
                .build();
    }
    
    
    private ApiInfo swaggerApiInfo() {
        return new ApiInfoBuilder()
                .title("Match and Trade API")
                .description("Welcome to <b>Match and Trade</b> REST API.")
                .termsOfServiceUrl("https://github.com/rafasantos/matchandtrade")
                .license("MIT License")
                .licenseUrl("https://github.com/rafasantos/matchandtrade")
                .version("2.0")
                .build();
    }
    
    @SuppressWarnings("unchecked")
	private Predicate<String> swaggerApiPaths() {
        return Predicates.or(
                PathSelectors.regex("/users.*")
        );
    }
}
