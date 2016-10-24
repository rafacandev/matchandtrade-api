# Welcome to Match and Trade

Match and Trade is a website where people post their unwanted items. Next, the website suggest possible trades where you can swap items with somebody else.

## Technology Stack
* [Java JDK][1]
* Spring Boot
* RESTful Service
* [Maven][2]
* Angular 2

## Quick Start
#### Requirements
* Java JDK
* Maven

Download the source code:

`git clone https://github.com/rafasantos/matchandtrade.git`

Build the source code. The files will be generated on the `/target` folder:

`mvn package`


Run the website:

`java -jar /target/webservice-0.0.1-SNAPSHOT.jar`

Access the website:

`http://localhost:8080/swagger-ui.html`

## Unit Test and Integration Test
Unit tests and integration tests are executed via `mvn` and configured with the maven plugins `maven-surefire-plugin` and `maven-failsafe-plugin` respectively.

Run the unit tests with the command `mvn test` and the integration tests with the command `mvn verify`.

## Maven Site
Maven is also capable to generate a very useful site which describing the project dependency tree, license, plugins, and testing reports.

To generate the maven site you need to issue the command `mvn site`. Open the file `target/site/index.xml` with your web-browser.

## Development Guide
Build and run the website using maven directly:

`mvn spring-boot:run`

Run unit tests:

`mvn test`

Run integration tests:

`mvn integration-test`

Generate maven web site. Files will be generated at `/target/site`:

`mvn site`

Swagger page:

`http://localhost:8080/swagger-ui.html`

You can change the logging level with the property `logging.level.root=DESIRED_LOGGING_LEVEL` or changing the `logback.xml`.

Examples:
```
mvn spring-boot:run -Dlogging.level.root=debug
mvn test -Dlogging.level.root=debug
java -jar /target/webservice-0.0.1-SNAPSHOT.jar -Dlogging.level.root=debug
```

## Writing Unit Test and Integration Test
As mentioned on the *Unit Test and Integration Test* section, tests are configured with the maven plugins `maven-surefire-plugin` and `maven-failsafe-plugin` respectively. To run unit tests use `mvn test` to run integration tests use `mvn verify`.

Unit tests should be placed on the folder `src/test/*` must be atomic and executed in any order. The file name also needs to end on `*UT.java`. Look at the file `src/test/java/com/matchandtrade/authentication/AuthenticationServletUT.java` for an example.

Integration tests are also placed on `src/test/*` and may depend on other integration tests (although dependency should be avoided) and must be executed within a test suite. The test suite file name needs to end on `*Suite.java` while the integration test needs to end on `*IT.java`. Look at the file `src/test/java/com/matchandtrade/rest/v1/controller/UserControllerSuite.java` for an example.

To run one single unit test use `mvn -Dtest=TEST_NAME verify`. To run one singe integration test use `mvn -Dit.test=INTEGRATION_TEST_NAME verify`. See maven [surefire][3] and [failsave][4] pulgins documentation. 


[1]: https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html "Install Java JDK"
[2]: http://maven.apache.org/install.html "Install Maven"
[3]: https://maven.apache.org/surefire/maven-surefire-plugin/examples/single-test.html "Maven surefire"
[4]: https://maven.apache.org/surefire/maven-failsafe-plugin/examples/single-test.html "Maven failsafe"