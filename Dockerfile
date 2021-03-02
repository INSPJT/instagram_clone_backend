FROM openjdk:11-jre-slim
LABEL maintainer="adamdoha@naver.com"

EXPOSE 8080
ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]