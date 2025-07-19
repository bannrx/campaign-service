# ------------ Build stage ------------
FROM gradle:8-jdk17 AS build
WORKDIR /home/campaign-service

# Copy campaign-service source code
COPY campaign-service/ ./

# Copy gradle properties (e.g., for private repo credentials)
COPY --chown=gradle:gradle docker-gradle.properties /home/gradle/.gradle/gradle.properties

# Build the application (jar will be created in build/libs/)
RUN ./gradlew clean build -x test

# ------------ Runtime stage ------------
FROM eclipse-temurin:17-jdk

# Install netcat
RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

WORKDIR /campaign-service

# Copy built jar from previous stage
COPY --from=build /home/campaign-service/build/libs/*.jar campaign-service.jar

# Add wait-for script
COPY wait-for.sh /campaign-service/wait-for.sh
RUN chmod +x /campaign-service/wait-for.sh

# Entry point waits for MySQL and then starts the app
ENTRYPOINT ["/campaign-service/wait-for.sh", "mysql", "3306", "java", "-jar", "campaign-service.jar"]
    