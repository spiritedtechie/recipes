FROM adoptopenjdk/openjdk11:latest
RUN mkdir /opt/app
COPY target/recipe-api-1.0-SNAPSHOT.jar /opt/app
CMD ["java", "-jar", "/opt/app/recipe-api-1.0-SNAPSHOT.jar"]
EXPOSE 8080