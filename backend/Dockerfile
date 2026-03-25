FROM --platform=linux/amd64 gradle:8-jdk21 AS builder
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build

FROM --platform=linux/amd64 eclipse-temurin:21-jre-alpine

# Create a non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set working directory
WORKDIR /app

COPY --from=builder /app/build/libs/backend-0.0.1-SNAPSHOT.jar app.jar

# Set ownership of the jar file
RUN chown appuser:appgroup app.jar

# Switch to non-root user
USER appuser

# Expose the port your application runs on (adjust as needed)
EXPOSE 8080

# Use ENTRYPOINT for the main command and CMD for default arguments
ENTRYPOINT ["java"]
CMD ["-Dspring.config.location=classpath:/application.properties,file:/app/application.properties", "-jar", "app.jar"]
