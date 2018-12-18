resource "aws_cloudwatch_log_group" "recipe" {
  name = "recipe"
  retention_in_days = 1
  tags = {
    Name = "cloudwatch-log-group-recipe"
  }
}