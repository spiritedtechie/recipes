Dependencies:

- Java 11
- Maven 3.6.0
- Terraform 0.11.11
- Docker

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