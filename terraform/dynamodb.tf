# Create a DynamoDB table
resource "aws_dynamodb_table" "recipes" {
  name           = "recipes"
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
    Name        = "dynamodb-table-recipes"
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