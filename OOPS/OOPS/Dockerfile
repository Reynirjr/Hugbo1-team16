# ===== Stage 1: Build with Maven wrapper on JDK 21 =====
FROM maven:3.9-eclipse-temurin-21 AS build

# Use a simple, consistent workdir
WORKDIR /app

# Copy only what's needed for dependency resolution first (better caching)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

# Now add the source
COPY src ./src

# Build the jar
RUN ./mvnw clean package -DskipTests


# ===== Stage 2: Runtime image (JRE 21) =====
FROM eclipse-temurin:21-jre

WORKDIR /app
VOLUME /tmp

# Copy the fat jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port Spring Boot uses (Render will map PORT env var)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
