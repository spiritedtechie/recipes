resource "aws_cloudwatch_log_group" "recipes" {
  name = "recipes"
  retention_in_days = 1
  tags = {
    Name = "cloudwatch-log-group-recipes"
  }
}