FROM adoptopenjdk/openjdk11:latest
RUN mkdir /opt/app
ARG JAR_FILE
COPY target/${JAR_FILE} /opt/app/recipe-api.jar
CMD ["java", "-jar", "/opt/app/recipe-api.jar"]
EXPOSE 8080