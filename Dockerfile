FROM openjdk:11-jdk-slim as builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar

FROM openjdk:11-jdk-slim
COPY --from=builder build/libs/*.jar insta.jar
VOLUME ["/var/log"]

EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=${profile}","/insta.jar"]
