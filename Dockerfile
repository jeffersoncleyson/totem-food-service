FROM maven:3.9.2-eclipse-temurin-17-alpine AS build
COPY totem-food-backend /usr/src/app/totem-food-backend
COPY totem-food-application /usr/src/app/totem-food-application
COPY totem-food-domain /usr/src/app/totem-food-domain
COPY totem-food-framework /usr/src/app/totem-food-framework
COPY pom.xml /usr/src/app/pom.xml
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:17.0.2-slim-buster
LABEL maintainer="Totem Food Service"
WORKDIR /opt/app
COPY --from=build /usr/src/app/totem-food-backend/target/*.jar totem-food-service.jar
ENTRYPOINT ["java","-jar","/opt/app/totem-food-service.jar"]