FROM eclipse-temurin:11-jre-alpine

COPY target/*.jar api-gateway.jar

ENTRYPOINT ["java", "-jar", "/api-gateway.jar"]