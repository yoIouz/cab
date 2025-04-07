FROM maven:3.9-eclipse-temurin-19-alpine AS builder
ARG SERVICE_NAME
WORKDIR /app
COPY pom.xml .
COPY common/pom.xml common/pom.xml
COPY ${SERVICE_NAME}/pom.xml ${SERVICE_NAME}/pom.xml
COPY common/src common/src
RUN mvn clean install -f common/pom.xml -DskipTests -T 4C
COPY ${SERVICE_NAME}/src /app/${SERVICE_NAME}/src
RUN mvn clean package -DskipTests -T 4C

FROM eclipse-temurin:19-jre-jammy
ARG SERVICE_NAME
ENV SERVICE_NAME=${SERVICE_NAME}
WORKDIR /app
COPY --from=builder /app/${SERVICE_NAME}/target/${SERVICE_NAME}-1.0-SNAPSHOT.jar /app/${SERVICE_NAME}-1.0-SNAPSHOT.jar
COPY entrypoint.sh /entrypoint.sh
ENTRYPOINT ["sh", "/entrypoint.sh"]
