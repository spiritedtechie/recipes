FROM adoptopenjdk/openjdk11:latest
RUN mkdir /opt/app
ARG JAR_FILE_LOCATION
COPY ${JAR_FILE_LOCATION} /opt/app/recipes-api.jar
CMD ["java", "-jar", "/opt/app/recipes-api.jar"]
EXPOSE 8080