#FROM amazoncorretto:19
#COPY build/libs/*.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

# Stage 1: Build the application with Gradle
FROM gradle:latest AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src/ ./src/
RUN gradle build --no-daemon

# Stage 2: Create a minimal Java runtime image
FROM openjdk:latest
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]