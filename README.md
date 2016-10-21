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


[1]: https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html "Install Java JDK"
[2]: http://maven.apache.org/install.html "Install Maven"
