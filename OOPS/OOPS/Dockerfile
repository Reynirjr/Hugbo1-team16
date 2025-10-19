# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Only copy pom first to leverage Docker layer caching
COPY pom.xml .
RUN mvn -q -B -e -DskipTests dependency:go-offline

# Now add sources and build
COPY src ./src
RUN mvn -q -B -DskipTests clean package

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0" \
    APP_HOME=/app
WORKDIR ${APP_HOME}

# copy the fat jar produced by Spring Boot (adjust name if your artifactId/version differs)
COPY --from=builder /OOPS/OOPS/target/*.jar app.jar



EXPOSE 8080


STOPSIGNAL SIGTERM


ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
