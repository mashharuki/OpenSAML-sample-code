# ============================================
# Stage 1: Build
# ============================================
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy pom.xml
COPY pom.xml ./

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B || true

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# ============================================
# Stage 2: Runtime (Lambda Web Adapter)
# ============================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Copy Lambda Web Adapter
COPY --from=public.ecr.aws/awsguru/aws-lambda-adapter:0.9.1 /lambda-adapter /opt/extensions/lambda-adapter

# Copy the built JAR
COPY --from=builder /app/target/*.jar app.jar

# Environment variables for Lambda Web Adapter
ENV PORT=8080
ENV READINESS_CHECK_PATH=/opensaml5-webprofile-demo/actuator/health
ENV ASYNC_INIT=true

# Spring Boot configuration
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Expose port
EXPOSE 8080

# Health check for local Docker
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/opensaml5-webprofile-demo/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
