# ── Stage 1: Build ──────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom first (layer caching — faster rebuilds)
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2: Run ────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built jar from stage 1
COPY --from=build /app/target/portfolio-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8085

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]