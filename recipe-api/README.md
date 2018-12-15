To build the project:    

    mvn clean package spring-boot:repackage

To run locally:

    mvn spring-boot:run

To run the application in a docker container:

    mvn clean package spring-boot:repackage
    docker build -t recipe-api .
    docker run -p 8080:8080 -d recipe-api

Add AWS credentials to ~/.aws/credentials under a profile named 'recipe-api'.

    [recipe-api]
    aws_access_key_id = blah
    aws_secret_access_key = blah
    region=eu-west-1