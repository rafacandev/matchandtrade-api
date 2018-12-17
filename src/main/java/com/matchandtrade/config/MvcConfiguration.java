package com.matchandtrade.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Add extra resource handler to expose {@code /essences/**} to the directory defined in the {@code essence.root.folder} property.
 * 
 * @author rafael.santos.bra@gmail.com
 * @see http://www.baeldung.com/spring-mvc-static-resources
 */
@Configuration
@EnableWebMvc
public class MvcConfiguration implements WebMvcConfigurer {
	public static final String ESSENCES_URL_PATTERN = "/matchandtrade-api/essences/**";
	private final Logger log = LoggerFactory.getLogger(MvcConfiguration.class);

	@Autowired
	private AppConfigurationProperties confiProperties;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String fileStorageRootFolderProperty = "file:" + confiProperties.filestorage.getEssenceRootPath();
		log.info("Exposing static files located at: [{}] with the pattern [{}].", fileStorageRootFolderProperty, ESSENCES_URL_PATTERN);
		registry.addResourceHandler(ESSENCES_URL_PATTERN).addResourceLocations(fileStorageRootFolderProperty);
	}
}