FROM openjdk:alpine
VOLUME /tmp
ADD matchandtrade-api-exec.jar /matchandtrade/matchandtrade-api-exec.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Xmx150m","-XX:MaxMetaspaceSize=100m","-jar","-Dmatchandtrade.config.file=/matchandtrade/config/matchandtrade.properties","/matchandtrade/matchandtrade-api-exec.jar"]
