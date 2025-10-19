# Importing JDK and copying required files
FROM openjdk:21-jdk-slim AS build
WORKDIR /OOPS/OOPS
COPY pom.xml .
COPY src src

# Copy Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Set execution permission for the Maven wrapper
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final Docker image using OpenJDK 19
FROM openjdk:21-jdk
VOLUME /tmp

# Copy the JAR from the build stage
COPY --from=build /OOPS/OOPS/target/*.jar OOPS.jar
ENTRYPOINT ["java","-jar","/OOPS.jar"]
EXPOSE 8080