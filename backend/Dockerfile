FROM gradle:8.13-alpine AS build
WORKDIR /opt/app/
COPY . .
RUN gradle clean build -x test

FROM eclipse-temurin:21
WORKDIR /opt/app/
ARG JAR_FILE=/opt/app/build/libs/energy-consumption-app-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} /opt/app/app.jar

# Enable remote debugging on port 5005
ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
EXPOSE 8080