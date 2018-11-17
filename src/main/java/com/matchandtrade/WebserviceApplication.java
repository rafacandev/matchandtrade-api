package com.matchandtrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.cloud.CloudAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.ldap.LdapDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.ldap.LdapRepositoriesAutoConfiguration;
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
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastJpaDependencyAutoConfiguration;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration;
import org.springframework.boot.autoconfigure.mobile.DeviceDelegatingViewResolverAutoConfiguration;
import org.springframework.boot.autoconfigure.mobile.DeviceResolverAutoConfiguration;
import org.springframework.boot.autoconfigure.mobile.SitePreferenceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
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
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketMessagingAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

import com.matchandtrade.authentication.AuthenticationServlet;
import com.matchandtrade.cli.AppCli;

@ServletComponentScan(basePackageClasses=AuthenticationServlet.class)
@SpringBootApplication
@EnableAutoConfiguration(exclude={
	// Required, matchandtrade-doc-maker fails if we exclude: 
	// EmbeddedServletContainerAutoConfiguration.class
	// WebMvcAutoConfiguration.class
	// DispatcherServletAutoConfiguration.class
	
	// Classed found on the auto-configuration report Positive Matches
	DataSourceAutoConfiguration.class,
	HttpEncodingAutoConfiguration.class,
	HypermediaAutoConfiguration.class,
	JacksonAutoConfiguration.class,
	JdbcTemplateAutoConfiguration.class,
	JmxAutoConfiguration.class,
	JtaAutoConfiguration.class,
	PersistenceExceptionTranslationAutoConfiguration.class,
	WebClientAutoConfiguration.class,
	WebSocketAutoConfiguration.class,
	
	// Classed found on the auto-configuration console Negative Matches
	ActiveMQAutoConfiguration.class,
	AopAutoConfiguration.class,
	ArtemisAutoConfiguration.class,
	BatchAutoConfiguration.class,
	CacheAutoConfiguration.class,
	CassandraAutoConfiguration.class,
	CassandraDataAutoConfiguration.class,
	CassandraRepositoriesAutoConfiguration.class,
	CloudAutoConfiguration.class,
	CouchbaseAutoConfiguration.class,
	CouchbaseDataAutoConfiguration.class,
	CouchbaseRepositoriesAutoConfiguration.class,
	DataSourceTransactionManagerAutoConfiguration.class,
	DeviceDelegatingViewResolverAutoConfiguration.class,
	DeviceResolverAutoConfiguration.class,
	ElasticsearchAutoConfiguration.class,
	ElasticsearchDataAutoConfiguration.class,
	ElasticsearchRepositoriesAutoConfiguration.class,
	EmbeddedLdapAutoConfiguration.class,
	EmbeddedMongoAutoConfiguration.class,
	FacebookAutoConfiguration.class,
	FallbackWebSecurityAutoConfiguration.class,
	FlywayAutoConfiguration.class,
	FreeMarkerAutoConfiguration.class,
	GroovyTemplateAutoConfiguration.class,
	GsonAutoConfiguration.class,
	H2ConsoleAutoConfiguration.class,
	HazelcastAutoConfiguration.class,
	HazelcastJpaDependencyAutoConfiguration.class,
	IntegrationAutoConfiguration.class,
	JerseyAutoConfiguration.class,
	JestAutoConfiguration.class,
	JmsAutoConfiguration.class,
	JndiConnectionFactoryAutoConfiguration.class,
	JndiDataSourceAutoConfiguration.class,
	JooqAutoConfiguration.class,
	KafkaAutoConfiguration.class,
	LdapAutoConfiguration.class,
	LdapDataAutoConfiguration.class,
	LdapRepositoriesAutoConfiguration.class,
	LinkedInAutoConfiguration.class,
	LiquibaseAutoConfiguration.class,
	MailSenderAutoConfiguration.class,
	MailSenderValidatorAutoConfiguration.class,
	MessageSourceAutoConfiguration.class,
	MongoAutoConfiguration.class,
	MongoDataAutoConfiguration.class,
	MongoRepositoriesAutoConfiguration.class,
	MustacheAutoConfiguration.class,
	Neo4jDataAutoConfiguration.class,
	Neo4jRepositoriesAutoConfiguration.class,
	OAuth2AutoConfiguration.class,
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
	TransactionAutoConfiguration.class,
	TwitterAutoConfiguration.class,
	ValidationAutoConfiguration.class,
	WebServicesAutoConfiguration.class,
	WebSocketMessagingAutoConfiguration.class,
	XADataSourceAutoConfiguration.class
})
public class WebserviceApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebserviceApplication.class);
	private static final String CONFIGURATION_FILE_PROPERTY_KEY = "--configFile or -cf or -Dspring.config.location";

	public static void main(String[] arguments) throws Throwable {
		// Handles the command line options.
		AppCli cli = null;
		try {
			cli = new AppCli(arguments);
		} catch (Exception e) {
			LOGGER.info("Not able to start application! ", e.getMessage(), e);
			System.exit(1);
		}

		// Load the correct configuration file as an environment property
		String configurationFile = loadConfigurationFileProperty(cli);

		// If line output message is interrupted (e.g: invalid command line); then, display message CommandLineOutputMessage 
		if (cli.isInterrupted()) {
			LOGGER.info(cli.getCommandLineOutputMessage());
		} else {
			// Proceed normally
			SpringApplication.run(WebserviceApplication.class);
			LOGGER.info("Using {}={}", CONFIGURATION_FILE_PROPERTY_KEY, configurationFile);
		}
	}

	private static String loadConfigurationFileProperty(AppCli cli) {
		String configurationFile = System.getProperty(CONFIGURATION_FILE_PROPERTY_KEY);
		if (configurationFile == null) {
			LOGGER.debug("Did not find {}", CONFIGURATION_FILE_PROPERTY_KEY);
		} else {
			LOGGER.debug("Found {}={}", CONFIGURATION_FILE_PROPERTY_KEY, configurationFile);
		}
		if (cli.configurationFilePath() != null) {
			LOGGER.debug("Found {}={}", CONFIGURATION_FILE_PROPERTY_KEY, cli.configurationFilePath());
			configurationFile = cli.configurationFilePath();
		}
		if (configurationFile == null) {
			LOGGER.info("Unable to start the application {} is required", CONFIGURATION_FILE_PROPERTY_KEY);
			System.exit(2);
		}
		System.setProperty("spring.config.location", configurationFile);
		return configurationFile;
	}

}
