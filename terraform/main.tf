terraform {
  backend "s3" {
    bucket = "terraform.spiritedtechie"
    key    = "state/terraform.tfstate"
    region = "eu-west-1"
  }
}

data "terraform_remote_state" "network" {
  backend = "s3"
  config {
    bucket = "terraform.spiritedtechie"
    key    = "state/terraform.tfstate"
    region = "eu-west-1"
  }
}

# Configure the AWS Provider
provider "aws" {
  region = "eu-west-1"
}