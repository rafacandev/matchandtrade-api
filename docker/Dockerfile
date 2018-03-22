FROM openjdk:alpine
VOLUME /tmp
ADD matchandtrade-web-api-exec.jar /matchandtrade/matchandtrade-web-api-exec.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Dmatchandtrade.config.file=/matchandtrade/config/matchandtrade.properties","/matchandtrade/matchandtrade-web-api-exec.jar"]
