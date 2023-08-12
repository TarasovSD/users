FROM amazoncorretto:19
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]