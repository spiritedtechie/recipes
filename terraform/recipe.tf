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

# Create a DynamoDB table
resource "aws_dynamodb_table" "recipe" {
  name           = "Recipe"
  billing_mode   = "PAY_PER_REQUEST"
  read_capacity  = 0
  write_capacity = 0
  hash_key       = "id"

  attribute {
    name = "id"
    type = "S"
  }

  attribute {
    name = "name"
    type = "S"
  }

  tags = {
    Name        = "dynamodb-table-recipe"
    Environment = "production"
  }

  global_secondary_index {
    name               = "idx_global_name"
    hash_key           = "name"
    write_capacity     = 0
    read_capacity      = 0
    projection_type    = "KEYS_ONLY"
  }
}

# Create user
resource "aws_iam_user" "recipe-api" {
  name = "recipe-api"
  path = "/recipe/"
}

data "aws_iam_policy_document" "dynamodb-recipe-table-access" {
  statement {
    actions = [
      "dynamodb:BatchGet*",
      "dynamodb:DescribeStream",
      "dynamodb:DescribeTable",
      "dynamodb:Get*",
      "dynamodb:Query",
      "dynamodb:Scan",
      "dynamodb:BatchWrite*",
      "dynamodb:Delete*",
      "dynamodb:Update*",
      "dynamodb:PutItem"
    ]

    resources = [
      "${aws_dynamodb_table.recipe.arn}",
    ]
  }

  statement {
    actions = [
      "dynamodb:Query"
    ]

    resources = [
      "${aws_dynamodb_table.recipe.arn}/index/*",
    ]
  }
}

resource "aws_iam_policy" "dynamodb-recipe-table-access" {
  name        = "policy-dynamodb-recipe-rw"
  path        = "/recipe/"
  description = "Read/write to Recipe table"
  policy      = "${data.aws_iam_policy_document.dynamodb-recipe-table-access.json}"
}

resource "aws_iam_policy_attachment" "recipe-api-user" {
  name       = "attachments-dynamodb-recipe-rw"
  users      = ["${aws_iam_user.recipe-api.name}"]
  policy_arn = "${aws_iam_policy.dynamodb-recipe-table-access.arn}"
}