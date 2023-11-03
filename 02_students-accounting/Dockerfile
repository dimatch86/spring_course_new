FROM openjdk:17-jdk-alpine3.14
WORKDIR /app
COPY ./target/students-accounting-0.0.1-SNAPSHOT.jar app.jar
COPY ./src/main/resources/students.txt /app/students.txt
ENV APP_CREATION_ENABLED=true
ENV PATH_TO_FILE=/app/students.txt
CMD ["java", "-jar", "app.jar"]