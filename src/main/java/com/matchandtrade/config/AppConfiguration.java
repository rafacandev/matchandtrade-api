package com.matchandtrade.config;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.matchandtrade.authentication.AuthenticationOAuth;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
public class AppConfiguration {
	
	private AppConfigurationProperties appProperties;

	public AppConfiguration() throws FileNotFoundException, IOException {
		appProperties = new AppConfigurationProperties(System.getProperties());
	}

	@Bean
	public ComboPooledDataSource dataSource() throws PropertyVetoException {
		ComboPooledDataSource result = new ComboPooledDataSource();
		result.setDriverClass(appProperties.getProperty(AppConfigurationProperties.Keys.DATA_SOURCE_DRIVER_CLASS));
		result.setJdbcUrl(appProperties.getProperty(AppConfigurationProperties.Keys.DATA_SOURCE_JDBC_URL));
		result.setPassword(appProperties.getProperty(AppConfigurationProperties.Keys.DATA_SOURCE_PASSWORD));
		result.setUser(appProperties.getProperty(AppConfigurationProperties.Keys.DATA_SOURCE_USER));
		return result;
	}
	
	@Bean
	public AuthenticationProperties authenticationProperties() {
		AuthenticationProperties result = new AuthenticationProperties();
		result.setClientId(appProperties.getProperty(AppConfigurationProperties.Keys.AUTHENTICATION_CLIENT_ID));
		result.setClientSecret(appProperties.getProperty(AppConfigurationProperties.Keys.AUTHENTICATION_CLIENT_SECRET));
		result.setRedirectURI(appProperties.getProperty(AppConfigurationProperties.Keys.AUTHENTICATION_CLIENT_REDIRECT_URL));
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Bean
	public AuthenticationOAuth authenticationOAuth() throws ReflectiveOperationException {
		Class authenticationOAuthClass = Class.forName(appProperties.getProperty(AppConfigurationProperties.Keys.AUTHENTICATION_OAUTH_CLASS));
		return (AuthenticationOAuth) authenticationOAuthClass.newInstance();
	}
	
	public AppConfigurationProperties getAppProperties() {
		return appProperties;
	}
	
	
	

	  @Bean
	  public EntityManagerFactory entityManagerFactory() throws PropertyVetoException {

	    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    vendorAdapter.setGenerateDdl(true);

	    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
	    factory.setJpaVendorAdapter(vendorAdapter);
	    factory.setPackagesToScan("com.matchandtrade.persistence.entity");
	    DataSource ds = (DataSource) dataSource();
	    factory.setDataSource(ds);
	    factory.afterPropertiesSet();

	    return factory.getObject();
	  }

	  @Bean
	  public PlatformTransactionManager transactionManager() throws PropertyVetoException {

	    JpaTransactionManager txManager = new JpaTransactionManager();
	    txManager.setEntityManagerFactory(entityManagerFactory());
	    return txManager;
	  }
	
	
	
	
}