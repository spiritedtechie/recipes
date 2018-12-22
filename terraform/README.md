Add AWS credentials to ~/.aws/credentials under a profile named 'recipes-deployer'.

    [recipes-deployer]
    aws_access_key_id = blah
    aws_secret_access_key = blah
    region=eu-west-1

Run as follows:

    ./terraform init
    ./terraform plan
    ./terraform apply