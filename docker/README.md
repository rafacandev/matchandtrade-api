Docker Image for Match and Trade Web API
========================================

Building a New Image
--------------------
When a new `matchandtrade-web-api` version is released we need to update
the docker image to reflect the newly released version.

1. Build `matchandtrade-web-api`
2. Copy the executable jar file to `docker/matchandtrade-web-api-exec.jar`
Example: `${HOME_DIR}/docker cp target/matchandtrade-web-api-1.3-exec.jar docker/matchandtrade-web-api-exec.jar`
3. Build the docker image with a new tag
Example: `${HOME_DIR}/docker sudo docker build -t rafaelsantosbra/matchandtrade-web-api:v1.3 .`
4. Start the docker image: `docker-compose up`
5. Verify if the application started correctly.
Example: `curl http://localhost:8080/rest/v1/trades`
6. Sign-in to dockerhub: `${HOME_DIR}/docker sudo docker login`
7. Push the new tag.
Example: `${HOME_DIR}/docker sudo docker push rafaelsantosbra/matchandtrade-web-api:v1.3`
