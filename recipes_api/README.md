To build the project and create an uber jar:

    mvn clean package spring-boot:repackage

Create a 'deployer' IAM user with the 'Administrator' role, and generate access keys too.

Add 'deployer' access keys to ~/.aws/credentials under a profile named 'recipes-deployer'.
This is used to deploy the docker container to the AWS ECR docker repository.

    [recipes-deployer]
    aws_access_key_id = blah
    aws_secret_access_key = blah
    region=eu-west-1

Ensure at least the ./terraform/terraform-dynamo-only.sh has been run to create the dynamodb tables,
and 'recipes-api' IAM policy and user. Generate an access key for the IAM user.

Add 'recipes-api' access keys to ~/.aws/credentials under a profile named 'recipes-api'.
This is used by the locally running application to access AWS services e.g. dynamodb.

    [recipes-api]
    aws_access_key_id = blah
    aws_secret_access_key = blah
    region=eu-west-1

To run locally in-process (not on Docker):

    mvn spring-boot:run

To build and deploy the container to the ECS repository.
Be sure to first create and then configure the ECS repository in the pom.xml:

    ./docker-deploy