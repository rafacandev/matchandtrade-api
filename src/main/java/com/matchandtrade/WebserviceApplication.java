package com.matchandtrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration.CglibAutoProxyConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration.JdkDynamicAutoProxyConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.cloud.CloudAutoConfiguration;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.data.solr.SolrRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.boot.autoconfigure.hateoas.HypermediaHttpMessageConverterConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastJpaDependencyAutoConfiguration;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration;
import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.hornetq.HornetQAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration;
import org.springframework.boot.autoconfigure.mobile.DeviceDelegatingViewResolverAutoConfiguration;
import org.springframework.boot.autoconfigure.mobile.DeviceResolverAutoConfiguration;
import org.springframework.boot.autoconfigure.mobile.SitePreferenceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.reactor.ReactorAutoConfiguration;
import org.springframework.boot.autoconfigure.security.FallbackWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.boot.autoconfigure.sendgrid.SendGridAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.autoconfigure.social.FacebookAutoConfiguration;
import org.springframework.boot.autoconfigure.social.LinkedInAutoConfiguration;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.boot.autoconfigure.social.TwitterAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.autoconfigure.velocity.VelocityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration.EmbeddedJetty;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration.EmbeddedUndertow;
import org.springframework.boot.autoconfigure.web.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketMessagingAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

import com.matchandtrade.authentication.AuthenticationServlet;
import com.matchandtrade.cli.AppCli;

@SuppressWarnings("deprecation")
@ServletComponentScan(basePackageClasses=AuthenticationServlet.class)
@SpringBootApplication
@EnableAutoConfiguration(exclude={
	// Classed found on the auto-configuration report Positive Matches
	AopAutoConfiguration.JdkDynamicAutoProxyConfiguration.class,
	DataSourceAutoConfiguration.class,
	HibernateJpaAutoConfiguration.class,
	HttpEncodingAutoConfiguration.class,
	HypermediaAutoConfiguration.class,
	HypermediaHttpMessageConverterConfiguration.class,
	JacksonAutoConfiguration.class,
	JdbcTemplateAutoConfiguration.class,
	JmxAutoConfiguration.class,
	JpaBaseConfiguration.class,
	JpaRepositoriesAutoConfiguration.class,
	JtaAutoConfiguration.class,
	PersistenceExceptionTranslationAutoConfiguration.class,
	WebClientAutoConfiguration.class,
	WebSocketAutoConfiguration.class,
	// Classed found on the auto-configuration console Negative Matches
	ActiveMQAutoConfiguration.class,
	ActiveMQAutoConfiguration.class,
	ActiveMQAutoConfiguration.class,
	AopAutoConfiguration.CglibAutoProxyConfiguration.class,
	AopAutoConfiguration.class,
	ArtemisAutoConfiguration.class,
	ArtemisAutoConfiguration.class,
	ArtemisAutoConfiguration.class,
	BatchAutoConfiguration.class,
	BatchAutoConfiguration.class,
	BatchAutoConfiguration.class,
	CacheAutoConfiguration.class,
	CacheAutoConfiguration.class,
	CacheAutoConfiguration.class,
	CassandraAutoConfiguration.class,
	CassandraAutoConfiguration.class,
	CassandraAutoConfiguration.class,
	CassandraDataAutoConfiguration.class,
	CassandraDataAutoConfiguration.class,
	CassandraDataAutoConfiguration.class,
	CassandraRepositoriesAutoConfiguration.class,
	CassandraRepositoriesAutoConfiguration.class,
	CassandraRepositoriesAutoConfiguration.class,
	CglibAutoProxyConfiguration.class,
	CglibAutoProxyConfiguration.class,
	CloudAutoConfiguration.class,
	CloudAutoConfiguration.class,
	CloudAutoConfiguration.class,
	CouchbaseAutoConfiguration.class,
	CouchbaseAutoConfiguration.class,
	CouchbaseCacheConfiguration.class,
	CouchbaseCacheConfiguration.class,
	CouchbaseDataAutoConfiguration.class,
	CouchbaseRepositoriesAutoConfiguration.class,
	CouchbaseRepositoriesAutoConfiguration.class,
	DataSourceAutoConfiguration.class,
	DataSourceAutoConfiguration.class,
	DataSourceAutoConfiguration.class,
	DataSourcePoolMetadataProvidersConfiguration.class,
	DataSourcePoolMetadataProvidersConfiguration.class,
	DeviceDelegatingViewResolverAutoConfiguration.class,
	DeviceDelegatingViewResolverAutoConfiguration.class,
	DeviceResolverAutoConfiguration.class,
	DeviceResolverAutoConfiguration.class,
	ElasticsearchAutoConfiguration.class,
	ElasticsearchAutoConfiguration.class,
	ElasticsearchDataAutoConfiguration.class,
	ElasticsearchDataAutoConfiguration.class,
	ElasticsearchRepositoriesAutoConfiguration.class,
	ElasticsearchRepositoriesAutoConfiguration.class,
	EmbeddedJetty.class,
	EmbeddedMongoAutoConfiguration.class,
	EmbeddedMongoAutoConfiguration.class,
	EmbeddedServletContainerAutoConfiguration.EmbeddedJetty.class,
	EmbeddedServletContainerAutoConfiguration.EmbeddedUndertow.class,
	EmbeddedUndertow.class,
	FacebookAutoConfiguration.class,
	FacebookAutoConfiguration.class,
	FallbackWebSecurityAutoConfiguration.class,
	FallbackWebSecurityAutoConfiguration.class,
	FlywayAutoConfiguration.class,
	FlywayAutoConfiguration.class,
	FreeMarkerAutoConfiguration.class,
	FreeMarkerAutoConfiguration.class,
	GroovyTemplateAutoConfiguration.class,
	GsonAutoConfiguration.class,
	H2ConsoleAutoConfiguration.class,
	HazelcastAutoConfiguration.class,
	HazelcastJpaDependencyAutoConfiguration.class,
	HibernateJpaAutoConfiguration.class,
	HibernateJpaAutoConfiguration.class,
	HibernateJpaAutoConfiguration.class,
	HornetQAutoConfiguration.class,
	HttpEncodingAutoConfiguration.class,
	HttpEncodingAutoConfiguration.class,
	HttpEncodingAutoConfiguration.class,
	HypermediaAutoConfiguration.class,
	HypermediaAutoConfiguration.class,
	HypermediaAutoConfiguration.class,
	HypermediaHttpMessageConverterConfiguration.class,
	HypermediaHttpMessageConverterConfiguration.class,
	HypermediaHttpMessageConverterConfiguration.class,
	IntegrationAutoConfiguration.class,
	JacksonAutoConfiguration.class,
	JacksonAutoConfiguration.class,
	JacksonAutoConfiguration.class,
	JdbcTemplateAutoConfiguration.class,
	JdbcTemplateAutoConfiguration.class,
	JdbcTemplateAutoConfiguration.class,
	JdkDynamicAutoProxyConfiguration.class,
	JdkDynamicAutoProxyConfiguration.class,
	JerseyAutoConfiguration.class,
	JestAutoConfiguration.class,
	JmsAutoConfiguration.class,
	JmxAutoConfiguration.class,
	JmxAutoConfiguration.class,
	JndiConnectionFactoryAutoConfiguration.class,
	JndiDataSourceAutoConfiguration.class,
	JooqAutoConfiguration.class,
	JpaBaseConfiguration.class,
	JpaBaseConfiguration.class,
	JpaRepositoriesAutoConfiguration.class,
	JpaRepositoriesAutoConfiguration.class,
	JtaAutoConfiguration.class,
	JtaAutoConfiguration.class,
	LinkedInAutoConfiguration.class,
	LiquibaseAutoConfiguration.class,
	MailSenderAutoConfiguration.class,
	MailSenderAutoConfiguration.class,
	MailSenderValidatorAutoConfiguration.class,
	MessageSourceAutoConfiguration.class,
	MessageSourceAutoConfiguration.class,
	MongoAutoConfiguration.class,
	MongoAutoConfiguration.class,
	MongoDataAutoConfiguration.class,
	MongoDataAutoConfiguration.class,
	MongoRepositoriesAutoConfiguration.class,
	MustacheAutoConfiguration.class,
	Neo4jDataAutoConfiguration.class,
	Neo4jRepositoriesAutoConfiguration.class,
	OAuth2AutoConfiguration.class,
	PersistenceExceptionTranslationAutoConfiguration.class,
	PersistenceExceptionTranslationAutoConfiguration.class,
	ProjectInfoAutoConfiguration.class,
	RabbitAutoConfiguration.class,
	ReactorAutoConfiguration.class,
	RedisAutoConfiguration.class,
	RedisRepositoriesAutoConfiguration.class,
	RepositoryRestMvcAutoConfiguration.class,
	SecurityAutoConfiguration.class,
	SecurityFilterAutoConfiguration.class,
	SendGridAutoConfiguration.class,
	SessionAutoConfiguration.class,
	SitePreferenceAutoConfiguration.class,
	SocialWebAutoConfiguration.class,
	SolrAutoConfiguration.class,
	SolrRepositoriesAutoConfiguration.class,
	SpringApplicationAdminJmxAutoConfiguration.class,
	ThymeleafAutoConfiguration.class,
	TwitterAutoConfiguration.class,
	VelocityAutoConfiguration.class,
	WebClientAutoConfiguration.class,
	WebClientAutoConfiguration.class,
	WebServicesAutoConfiguration.class,
	WebSocketAutoConfiguration.class,
	WebSocketAutoConfiguration.class,
	WebSocketMessagingAutoConfiguration.class,
	XADataSourceAutoConfiguration.class
})
public class WebserviceApplication {
	
	public static void main(String[] arguments) {
		// TODO: Create an abstraction to handle Console outputs instead of use System.err or System.out
		// Handles the command line options.
		AppCli cli = null;
		try {
			cli = new AppCli(arguments);
		} catch (Throwable t) {
			System.err.println("Not able to start application! " + t.getMessage());
			System.err.println("Exiting the application with error code [1]. Not able to process command line options.");
			System.exit(1);
		}
		
		// If line output message is interrupted; then, display message 
		if (cli.isInterrupted()) {
			System.out.println(cli.getCommandLineOutputMessage());
		} else {
			// Proceed normally
			SpringApplication.run(WebserviceApplication.class, arguments);
		}
	}
}