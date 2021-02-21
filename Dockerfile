FROM adoptopenjdk/openjdk13:debian-slim as BUILD_IMAGE

ENV GRADLE_OPTS "-Dorg.gradle.daemon=false"
ENV APP_HOME /app
RUN mkdir $APP_HOME
WORKDIR $APP_HOME

# Copy src and build project
COPY settings.gradle.kts build.gradle.kts gradlew $APP_HOME/
ADD gradle $APP_HOME/gradle
ADD src $APP_HOME/src
RUN $APP_HOME/gradlew clean build

# We are using multi-stage build to improve the docker image size
# Please refer to: https://docs.docker.com/develop/develop-images/multistage-build/
FROM adoptopenjdk/openjdk13:debian-slim
WORKDIR /app

COPY --from=BUILD_IMAGE /app/build/libs/reactive-0.0.1-SNAPSHOT.jar /app/rx.jar
