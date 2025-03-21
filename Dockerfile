FROM maven:3.9-eclipse-temurin-19-alpine AS builder
ARG SERVICE_NAME
WORKDIR /app
#COPY common/pom.xml common/pom.xml
#COPY common/src common/src
COPY pom.xml .
COPY ${SERVICE_NAME}/pom.xml ${SERVICE_NAME}/pom.xml
COPY ${SERVICE_NAME}/src /app/${SERVICE_NAME}/src
#RUN mvn clean install -f common/pom.xml -DskipTests
RUN mvn clean package -DskipTests

FROM openjdk:19-jdk-slim
ARG SERVICE_NAME
ENV SERVICE_NAME=${SERVICE_NAME}
WORKDIR /app
COPY entrypoint.sh /entrypoint.sh
COPY --from=builder /app/${SERVICE_NAME}/target/${SERVICE_NAME}-1.0-SNAPSHOT.jar /app/${SERVICE_NAME}-1.0-SNAPSHOT.jar
ENTRYPOINT ["sh", "/entrypoint.sh"]
