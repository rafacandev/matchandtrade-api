package com.matchandtrade;

import com.matchandtrade.authentication.AuthenticationServlet;
import com.matchandtrade.cli.AppCli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.cloud.CloudServiceConnectorsAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.ldap.LdapRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.data.solr.SolrRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastJpaDependencyAutoConfiguration;
import org.springframework.boot.autoconfigure.influx.InfluxDbAutoConfiguration;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.reactor.core.ReactorCoreAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityRequestMatcherProviderAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.sendgrid.SendGridAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.client.WebServiceTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.reactive.WebSocketReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan(basePackageClasses=AuthenticationServlet.class)
@SpringBootApplication
@EnableAutoConfiguration(exclude = {
	ActiveMQAutoConfiguration.class,
	AopAutoConfiguration.class,
	ArtemisAutoConfiguration.class,
	BatchAutoConfiguration.class,
	CassandraAutoConfiguration.class,
	CassandraDataAutoConfiguration.class,
	CassandraReactiveDataAutoConfiguration.class,
	CassandraReactiveRepositoriesAutoConfiguration.class,
	CassandraRepositoriesAutoConfiguration.class,
	ClientHttpConnectorAutoConfiguration.class,
	CloudServiceConnectorsAutoConfiguration.class,
	CouchbaseAutoConfiguration.class,
	CouchbaseDataAutoConfiguration.class,
	CouchbaseReactiveDataAutoConfiguration.class,
	CouchbaseReactiveRepositoriesAutoConfiguration.class,
	CouchbaseRepositoriesAutoConfiguration.class,
	ElasticsearchAutoConfiguration.class,
	ElasticsearchDataAutoConfiguration.class,
	ElasticsearchRepositoriesAutoConfiguration.class,
	EmbeddedLdapAutoConfiguration.class,
	EmbeddedMongoAutoConfiguration.class,
	EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
	ErrorWebFluxAutoConfiguration.class,
	FlywayAutoConfiguration.class,
	FreeMarkerAutoConfiguration.class,
	GroovyTemplateAutoConfiguration.class,
	GsonAutoConfiguration.class,
	HazelcastAutoConfiguration.class,
	HazelcastJpaDependencyAutoConfiguration.class,
	HttpHandlerAutoConfiguration.class,
	InfluxDbAutoConfiguration.class,
	IntegrationAutoConfiguration.class,
	JdbcRepositoriesAutoConfiguration.class,
	JerseyAutoConfiguration.class,
	JestAutoConfiguration.class,
	JmsAutoConfiguration.class,
	JndiConnectionFactoryAutoConfiguration.class,
	JooqAutoConfiguration.class,
	JsonbAutoConfiguration.class,
	KafkaAutoConfiguration.class,
	LdapAutoConfiguration.class,
	LdapRepositoriesAutoConfiguration.class,
	LiquibaseAutoConfiguration.class,
	MailSenderAutoConfiguration.class,
	MailSenderValidatorAutoConfiguration.class,
	MessageSourceAutoConfiguration.class,
	MongoAutoConfiguration.class,
	MongoDataAutoConfiguration.class,
	MongoReactiveAutoConfiguration.class,
	MongoReactiveDataAutoConfiguration.class,
	MongoReactiveRepositoriesAutoConfiguration.class,
	MongoRepositoriesAutoConfiguration.class,
	MustacheAutoConfiguration.class,
	Neo4jDataAutoConfiguration.class,
	Neo4jRepositoriesAutoConfiguration.class,
	OAuth2ClientAutoConfiguration.class,
	OAuth2ResourceServerAutoConfiguration.class,
	QuartzAutoConfiguration.class,
	RabbitAutoConfiguration.class,
	ReactiveOAuth2ClientAutoConfiguration.class,
	ReactiveOAuth2ResourceServerAutoConfiguration.class,
	ReactiveSecurityAutoConfiguration.class,
	ReactiveUserDetailsServiceAutoConfiguration.class,
	ReactiveWebServerFactoryAutoConfiguration.class,
	ReactorCoreAutoConfiguration.class,
	RedisAutoConfiguration.class,
	RedisReactiveAutoConfiguration.class,
	RedisRepositoriesAutoConfiguration.class,
	RepositoryRestMvcAutoConfiguration.class,
	RestClientAutoConfiguration.class,
	SecurityAutoConfiguration.class,
	SecurityFilterAutoConfiguration.class,
	SecurityRequestMatcherProviderAutoConfiguration.class,
	SendGridAutoConfiguration.class,
	SessionAutoConfiguration.class,
	SolrAutoConfiguration.class,
	SolrRepositoriesAutoConfiguration.class,
	SpringApplicationAdminJmxAutoConfiguration.class,
	TaskSchedulingAutoConfiguration.class,
	ThymeleafAutoConfiguration.class,
	UserDetailsServiceAutoConfiguration.class,
	ValidationAutoConfiguration.class,
	WebClientAutoConfiguration.class,
	WebFluxAutoConfiguration.class,
	WebServiceTemplateAutoConfiguration.class,
	WebServicesAutoConfiguration.class,
	WebSocketMessagingAutoConfiguration.class,
	WebSocketReactiveAutoConfiguration.class,
	WebSocketServletAutoConfiguration.class,
	XADataSourceAutoConfiguration.class
})
public class WebserviceApplication {
	private static final Logger log = LoggerFactory.getLogger(WebserviceApplication.class);
	private static final String CONFIGURATION_FILE_PROPERTY_DESCRIPTION = "--configFile or -cf or -Dspring.config.location";
	private static final String CONFIGURATION_FILE_PROPERTY_KEY = "spring.config.location";

	public static void main(String[] arguments) {
		// Handles the command line options.
		AppCli cli = null;
		try {
			cli = new AppCli(arguments);
		} catch (Exception e) {
			log.info("Not able to start application! ", e.getMessage(), e);
			System.exit(1);
		}

		// Load the correct configuration file as an environment property
		String configurationFile = loadConfigurationFileProperty(cli);

		if (configurationFile == null || cli.isInterrupted()) {
			log.debug("Did not fin {}", CONFIGURATION_FILE_PROPERTY_DESCRIPTION);
			log.info(cli.getCommandLineOutputMessage());
		} else {
			// Proceed normally
			SpringApplication.run(WebserviceApplication.class);
		}
	}

	private static String loadConfigurationFileProperty(AppCli cli) {
		String configurationFile;
		if (cli.configurationFilePath() != null) {
			log.debug("Found {}={}", CONFIGURATION_FILE_PROPERTY_DESCRIPTION, cli.configurationFilePath());
			configurationFile = cli.configurationFilePath();
			System.setProperty(CONFIGURATION_FILE_PROPERTY_KEY, configurationFile);
		} else {
			configurationFile = System.getenv(CONFIGURATION_FILE_PROPERTY_KEY);
			if (configurationFile == null) {
				configurationFile = System.getProperty(CONFIGURATION_FILE_PROPERTY_KEY);
			}
		}
		return configurationFile;
	}
}
