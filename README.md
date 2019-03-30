# Dependencies:

- Java 11
- Maven 3.6.0
- Terraform 0.11.11
- Docker e.g. Docker Desktop on Mac

# Docker Containers

The API, along with prometheus & grafana can be run locally on Docker.

To build and run a Dockerised version of the application and its dependencies use:

    ./docker-compose build
    ./docker-compose up -d
    
To stop the containers:

    ./docker-compose down
    
To inspect a container:

    docker container ls
    docker container inspect <CONTAINER ID>

To SSH into a container e.g. for debugging:

    docker container exec -it <CONTAINER_NAME> /bin/bash
    
# Web Application

In the web_app directory:
    
    npm start
   
This runs the app in the development mode, and should fire up the React app in the browser.

# Monitoring

Once the Docker containers have started:

Open [http://localhost:9090](http://localhost:9090) for Prometheus

Open [http://localhost:3000](http://localhost:3000) for Grafana