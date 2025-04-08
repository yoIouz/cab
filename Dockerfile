FROM maven:3.9-eclipse-temurin-19-alpine AS builder
ARG SERVICE_NAME
WORKDIR /app
COPY . .
RUN mvn clean package -pl ${SERVICE_NAME} -am -DskipTests

FROM eclipse-temurin:19-jre-jammy
ARG SERVICE_NAME
ENV SERVICE_NAME=${SERVICE_NAME}
WORKDIR /app
COPY --from=builder /app/${SERVICE_NAME}/target/*.jar /app/${SERVICE_NAME}.jar
COPY entrypoint.sh /entrypoint.sh
ENTRYPOINT ["sh", "/entrypoint.sh"]
